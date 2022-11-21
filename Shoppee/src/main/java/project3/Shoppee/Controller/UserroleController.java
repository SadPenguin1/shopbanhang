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

import project3.Shoppee.Entity.UserRole;
import project3.Shoppee.Repository.UserRepo;
import project3.Shoppee.Repository.UserRoleRepo;



@Controller
@RequestMapping("/admin/userrole")
public class UserroleController {
	
	
	@Autowired
	UserRoleRepo userroleRepo;
	@Autowired
	UserRepo userRepo;


	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("userList", userRepo.findAll());	
		model.addAttribute("Userrole" ,new UserRole());
		return "userrole/add";
	}
  
	@PostMapping("/add")
	public String add(@ModelAttribute @Valid UserRole userrole,BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "userrole/add";
		}
		userroleRepo.save(userrole);
		return "redirect:/admin/userrole/search";			
	}
		
	@GetMapping("/delete")
	public String delete(Model model,@Param("id") int id) {
		userroleRepo.deleteById(id);
		return "redirect:/admin/userrole/search";		
	}
	@GetMapping("/search")
	public String search(Model model,
			@RequestParam(value = "id",required=false) Integer id,
			@RequestParam(value= "role",required=false) String r,
		
			@RequestParam(value= "page",required=false) Integer page,
			@RequestParam(value="size",required=false) Integer size) {
		page = (page==null ? 0:page);
		size = (size==null ? 10:size);
		
		
		Pageable pageable= PageRequest.of(page, size,Sort.by(Direction.ASC,"id"));
		
		if( id != null ) {
			List<UserRole> Userroles = userroleRepo.findAllById(Arrays.asList(id));
			
			model.addAttribute("count", Userroles.size());
			model.addAttribute("totalPages",1);
			model.addAttribute("roleList",userroleRepo.findAll());
		}else{		
			Page<UserRole> pageRS = null;
			
			if (StringUtils.hasText(r)){
				pageRS = userroleRepo.searchByRole( "%" + r + "%",pageable);	
			}else {
				pageRS = userroleRepo.findAll(pageable);
			}

			model.addAttribute("count", pageRS.getTotalElements());
			model.addAttribute("totalPages", pageRS.getTotalPages());
			model.addAttribute("roleList",pageRS.getContent());
		}
		
		model.addAttribute("id",id);
		model.addAttribute("role",r);

		model.addAttribute("size",size);
		model.addAttribute("page",page);
		
		model.addAttribute("userList",userroleRepo.findAll());

		return "userrole/search";
	}
	@GetMapping("/edit")
	public String edit(Model model,@RequestParam("id")int id) {
		UserRole userrole = userroleRepo.findById(id).orElse(null);
		model.addAttribute("role",userrole);
		model.addAttribute("userList", userRepo.findAll());
		return "userrole/edit";
	}
	
	@PostMapping("/edit")
	public String edit(@ModelAttribute("Userrole") UserRole userrole) {
		userroleRepo.save(userrole);
		return "redirect:/admin/userrole/search";
	}
}
