package project3.Shoppee.Controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project3.Shoppee.Entity.Category;
import project3.Shoppee.Repository.CategoryRepo;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {
	
	@Autowired
	CategoryRepo categoryRepo;
	
	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("category", new Category());
		return "category/add";
	}
	
	@PostMapping("/add")
	public String add(@ModelAttribute @Valid Category category,BindingResult bindingResult ) {
		if(bindingResult.hasErrors()) {
			return "category/add";
		}
		categoryRepo.save(category);
		return "redirect:/admin/category/search";
	}
	
	@GetMapping("/edit")
	public String edit(Model model,@RequestParam("id") int id) {
		Category category = categoryRepo.findById(id).orElse(null);
		model.addAttribute("category", category);
		return "category/edit";
	}
	
	@PostMapping("/edit")
	public String edit(@ModelAttribute @Valid Category category,BindingResult bindingResult ) {		
		categoryRepo.save(category);
		return "redirect:/admin/category/search";
	}
	
	@GetMapping("/delete")
	public String delete(@Param("id") int id) {
		categoryRepo.deleteById(id);
		return "redirect:/admin/category/search";

	}
	
	@GetMapping("/search")
	public String search(Model model,
			@RequestParam(value="id",required = false) Integer id,
			@RequestParam(value="categoryName",required = false) String categoryName,
			
			@RequestParam(value="page",required = false) Integer page,
			@RequestParam(value="size",required = false) Integer size){
		page = (page == null ? 0:page);
		size = (size == null ? 10:size);
		
		Pageable pageable = PageRequest.of(page,size , Sort.by(Direction.ASC,"id"));
		
		if(id != null) {
			List<Category> categories = categoryRepo.findAllById(Arrays.asList(id));
			
			model.addAttribute("count" ,categories.size());
			model.addAttribute("totalPages" ,1);
			model.addAttribute("categoryList" ,categories);
		}else{
			
				Page<Category> pageRS = null;
				
				if(StringUtils.hasText(categoryName)) {
					pageRS = categoryRepo.searchByName("%" + categoryName + "%" ,pageable);		
				}else {
					pageRS = categoryRepo.findAll(pageable);
				}
				
				model.addAttribute("count" ,pageRS.getTotalElements());
				model.addAttribute("totalPages" ,pageRS.getTotalPages());
				model.addAttribute("categoryList",pageRS.getContent());		
		}
		
		model.addAttribute("id",id);
		model.addAttribute("categoryName",categoryName);
		
		model.addAttribute("size",size);
		model.addAttribute("page",page);
			
		return "category/search";

	}

	
	
	
}
