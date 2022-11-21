  package project3.Shoppee.Controller;


import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project3.Shoppee.Entity.User;
import project3.Shoppee.Repository.UserRepo;

@Controller
@RequestMapping("/admin/user")
public class UserController {

	@Autowired
	UserRepo userRepo ;


	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("user", new User());
		return "user/add";
	}
  
	@PostMapping("/add")
	public String add(@ModelAttribute User user,BindingResult bindingResult)   {
	
			if(bindingResult.hasErrors()) {
				return "user/add";
			}
			user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

			userRepo.save(user);
			return "redirect:/admin/user/search";			
		}
	
	@GetMapping("/delete")
	//@Secured
	public String delete(Model model,@Param("id") int id,Principal principal) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null && !(auth instanceof AnonymousAuthenticationToken)){
			// get thong tin ra
			//principal : chinh la user tu LoginServer tra ve			
		}
		
		userRepo.deleteById(id);
		return "redirect:/admin/user/search";		
	}
	
//	@PreAuthorize("hasAuthority('ADMIN')")
//	@PostAuthorize("returnObject != null || #id != null")
	@GetMapping("/search")
	public String search(Model model,
			@RequestParam(value = "id",required=false) Integer id,
			@RequestParam(value= "name",required=false) String n,
			@RequestParam(value= "email",required=false) String e,
			

			@RequestParam(value= "page",required=false) Integer page,
			@RequestParam(value="size",required=false) Integer size) {
		page = (page==null ? 0:page);
		size = (size==null ? 10:size);
		
		Pageable pageable= PageRequest.of(page, size,Sort.by(Direction.ASC,"id"));
		
		if( id != null ) {
			List<User> users = userRepo.findAllById(Arrays.asList(id));
			
			model.addAttribute("count", users.size());
			model.addAttribute("totalPages",1);
			model.addAttribute("userList",users);
		}else{		
			Page<User> pageUser = null;
			
			if(StringUtils.hasText(n) && StringUtils.hasText(e) ) {
				pageUser = userRepo.searchByNameAndEmail( "%" + n + "%","%" + e + "%",  pageable);				
			}else if (StringUtils.hasText(n) ){
				pageUser = userRepo.searchByName( "%" + n + "%",  pageable);	
			}else if (StringUtils.hasText(e)) {
				pageUser = userRepo.searchByEmail( "%" + e + "%", pageable);			
			}else {
				pageUser = userRepo.findAll(pageable);
			}

			model.addAttribute("count", pageUser.getTotalElements());
			model.addAttribute("totalPages", pageUser.getTotalPages());
			model.addAttribute("userList",pageUser.getContent());
		}
		
		model.addAttribute("id",id);
		model.addAttribute("name",n);	
		model.addAttribute("email",e);	


		model.addAttribute("size",size);
		model.addAttribute("page",page);
		

		return "user/search";
	}
	@GetMapping("/edit")
	public String edit(Model model,@RequestParam("id")int id) {
		User u = userRepo.findById(id).orElse(null);
		model.addAttribute("user",u);
		return "user/edit";
	}
	
	@PostMapping("/edit")
	public String edit(@ModelAttribute User u,BindingResult bindingResult)   {
			u.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));
			userRepo.save(u);
			return "redirect:/admin/user/search";
		}
	
}
	
