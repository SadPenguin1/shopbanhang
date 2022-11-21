package project3.Shoppee.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.query.Param;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import project3.Shoppee.Entity.Category;
import project3.Shoppee.Entity.Product;
import project3.Shoppee.Entity.User;
import project3.Shoppee.Repository.CategoryRepo;
import project3.Shoppee.Repository.ProductRepo;
import project3.Shoppee.dto.ResponseDTO;

@Controller
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	CategoryRepo categoryRepo;
	
	@Autowired
	ProductRepo productRepo;
	
	
	@KafkaListener(id = "statiscGroup",topics = "notification")
	public void listen(User u) {
		System.out.println(u.getName());
	}
	
//	@GetMapping("/test-api")
//	public String testAPI() {
//		//wedtemplate,okhttp,openf
//		RestTemplate restTemplate = new RestTemplate();
//		String apiURL = "http://localhost:8081/api/hi";
//		
//		Category category = restTemplate.getForObject(apiURL,Category.class );
//		System.out.println(category.getName());
//		return "home.html";
//	}
	
	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("product", new Product());
		model.addAttribute("categoryList",categoryRepo.findAll());
		return "product/add";
	}
	
	
	@PostMapping("/add")
	public String add(@ModelAttribute @Valid Product product,@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException  {
		
		if(!file.isEmpty()) {
			final String UPLOAD_FOLDEL ="/C:/Shoppee/";
			String filename = file.getOriginalFilename();
			File newFile = new File(UPLOAD_FOLDEL +filename);
			
			file.transferTo(newFile);
			
		product.setImage(filename);		
		}
		productRepo.save(product);
		return "redirect:/product/search";
	
	}	
	
	@GetMapping("/download")
	public void download(@RequestParam("filename") String filename,HttpServletResponse response) throws IOException {
		final String UPLOAD_FOLDEL = "/C:/Shoppee/";
		
		File file = new File(UPLOAD_FOLDEL +filename);
		Files.copy(file.toPath(),response.getOutputStream());
	}
	
	@GetMapping("/edit")
	public String edit(Model model,@RequestParam("id") int id) {
		Product product = productRepo.findById(id).orElse(null);
		model.addAttribute("product", product);
		return "product/edit";
	}
	
	@PostMapping("/edit")
	public String edit(@ModelAttribute Product editProduct) throws IllegalStateException, IOException  {
		Product current = productRepo.findById(editProduct.getId()).orElse(null);
		
		if(current != null) {
			if(!editProduct.getFile().isEmpty()) {
				final String UPLOAD_FOLDEL ="/C:/Shoppee/";
				
				String filename = editProduct.getFile().getOriginalFilename();
				File newFile = new File(UPLOAD_FOLDEL +filename);
				
				editProduct.getFile().transferTo(newFile);
				
				current.setImage(filename);
			}
			current.setName(editProduct.getName());
			current.setDescription(editProduct.getDescription());
			current.setPrice(editProduct.getPrice());
			

			productRepo.save(current);
		}
		return "redirect:/product/search";		
	}
	
	@GetMapping("/delete")
	public String delete(@Param("id") int id) {
		productRepo.deleteById(id);
		return "redirect:/product/search";

	}
	
	@GetMapping("/search")
	public String search(Model model,
			@RequestParam(value="id",required = false) Integer id,
			
			@RequestParam(value="productName",required = false) String productName,
			@RequestParam(value="categoryName", required = false) String categoryName,
			@RequestParam(value="categoryId", required = false) Integer categoryId,	

			@RequestParam(value="page",required = false) Integer page,
			@RequestParam(value="size",required = false) Integer size){
		page = (page == null ? 0:page);
		size = (size == null ? 10:size);
		
		Pageable pageable = PageRequest.of(page,size , Sort.by(Direction.ASC,"id"));
		
		if(id != null) {
			List<Product> products = productRepo.findAllById(Arrays.asList(id));
			
			model.addAttribute("count" ,products.size());
			model.addAttribute("totalPages" ,1);
			model.addAttribute("productList" ,products);
		}else{
			
				Page<Product> pageRS = null;
				
				if(StringUtils.hasText(productName)) {
					pageRS = productRepo.searchByName("%" + productName + "%" ,pageable);	
				}else if( categoryId != null) {				
					pageRS = productRepo.searchByCategoryId(categoryId, pageable);
				}else {
					pageRS = productRepo.findAll(pageable);
				}
				
				model.addAttribute("count" ,pageRS.getTotalElements());
				model.addAttribute("totalPages" ,pageRS.getTotalPages());
				model.addAttribute("productList",pageRS.getContent());		
		}
		
		model.addAttribute("id",id);
		model.addAttribute("productName",productName);
		model.addAttribute("categoryId",categoryId);
		model.addAttribute("categoryName",categoryName);

	
		model.addAttribute("size",size);
		model.addAttribute("page",page);
		
		model.addAttribute("categoryList",categoryRepo.findAll());

			
		return "product/search";

	}

	
	
	
}
