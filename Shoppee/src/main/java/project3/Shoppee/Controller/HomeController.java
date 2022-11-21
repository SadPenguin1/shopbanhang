package project3.Shoppee.Controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project3.Shoppee.Entity.Bill;
import project3.Shoppee.Entity.Billitem;
import project3.Shoppee.Entity.Product;
import project3.Shoppee.Entity.User;
import project3.Shoppee.Repository.BillRepo;
import project3.Shoppee.Repository.BillitemRepo;
import project3.Shoppee.Repository.CategoryRepo;
import project3.Shoppee.Repository.ProductRepo;
import project3.Shoppee.Repository.UserRepo;

@Controller
public class HomeController {
	

	@Autowired
	Environment environment ;
	
	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	CategoryRepo categoryRepo;
	@Autowired
	BillRepo billRepo;
	@Autowired
	UserRepo userRepo;
	@Autowired
	BillitemRepo billitemRepo;
	
//	@GetMapping(value ="/home")
//	public String home(HttpServletRequest request) {
//		request.setAttribute("msg", environment.getProperty("message"));
//		return "home";
//	}
//	
	@GetMapping("/admin/home")
	public String home(HttpServletRequest request) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null && !(auth instanceof AnonymousAuthenticationToken)){
			// get thong tin ra
			//principal : chinh la user tu LoginServer tra ve
			
		 if(request.isUserInRole("Admin")) {
			 //trongDB du lieu chu ROLE_ADMIN
			 return "home";			 
		 }
		 
		 //dung authority
		 List<String> roles = auth.getAuthorities().stream().map(p -> p.getAuthority()).collect(Collectors.toList());
		 if(roles.contains("ADMIN"))
			 return "home";
		}
		return null;
		
		
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

			
		return "searchproduct";

	}	

	@GetMapping("/payment")
	public String payment(HttpServletRequest request, HttpSession session,Principal principal) {
		if (session.getAttribute("cart") != null) {
			Map<Integer, Billitem> map = (Map<Integer, Billitem>) session.getAttribute("cart");
			
			Bill bill = new Bill();
			bill.setBuyDate(new Date());
			bill.setUser(userRepo.findByUsername(principal.getName()));

			billRepo.save(bill);
			for (Entry<Integer, Billitem> entry : map.entrySet()) {
				Billitem billitem = entry.getValue();
				billitem.setBill(bill);
				billitemRepo.save(billitem);
			}
	}
		return "redirect:payment-success.html";
	}
	
//	
//	@PostMapping("/payment")
//	public String payment( HttpSession session,@ModelAttribute Receiver receiver) {
//		
//		User  user = (User) session.getAttribute("user");
//		Bill bill = new Bill();
//		bill.setBuyDate(new Date());
//		bill.setUser(user);
//		billRepo.save(bill);
//		
//		receiver.setUser(user);
//		receiver.setBill(bill);
//		receiverRepo.save(receiver);
//		
//
//		OrderDTO orderDTO = (OrderDTO) session.getAttribute("cart");
//		List<OrderItemDTO> itemDTOs = orderDTO.getItemDTOs();
//		for (OrderItemDTO orderItemDTO : itemDTOs) {
//			Billitems billItems = new Billitems();
//			billItems.setBill(bill);
//			billItems.setProduct(orderItemDTO.getProduct());
//			billItems.setQuantity(orderItemDTO.getNumber());
//			billitemRepo.save(billItems);	
//		}
//		
//	    session.removeAttribute("cart");
//		return "redirect:/";
//	}
	
	

//	@GetMapping("/user/bill")
//	public String billuser(HttpSession session, Model model, @RequestParam(name = "id", required = false) Integer id ) {
//		
//		if (id != null) {
//			model.addAttribute("BDList",billitemRepo.findById(id));
//			model.addAttribute("newid", id);
//		}
//
//		if (session.getAttribute("user") !=null) {
//			model.addAttribute("user", session.getAttribute("user"));
//		}
//		
//		User user = (User) session.getAttribute("user");
//		model.addAttribute("list", billRepo.findById(user));
//		return "user/bill";
//	}
//	
//	

	
}
