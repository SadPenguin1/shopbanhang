package project3.Shoppee.Controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project3.Shoppee.Entity.Bill;
import project3.Shoppee.Repository.BillRepo;
import project3.Shoppee.Repository.UserRepo;

@Controller
@RequestMapping("/admin/bill")
public class BillController {
	
	@Autowired
	BillRepo billRepo;
	@Autowired
	UserRepo userRepo;


	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("userList", userRepo.findAll());	
		return "bill/add";
	}
  
	@PostMapping("/add")
	public String add(@ModelAttribute @Valid Bill bill,BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "bill/add";
		}
		billRepo.save(bill);
		return "redirect:/admin/bill/search";			
	}
		
	@GetMapping("/delete")
	public String delete(Model model,@Param("id") int id) {
		billRepo.deleteById(id);
		return "redirect:/admin/bill/search";		
	}
	@GetMapping("/search")
	public String search(Model model,
			@RequestParam(value = "id",required=false) Integer id,
			
			@RequestParam(value= "startDate",required=false)@DateTimeFormat(pattern ="dd/MM/yyyy HH:mm") Date start ,
			@RequestParam(value= "endDate",required=false)@DateTimeFormat(pattern ="dd/MM/yyyy HH:mm")  Date end ,

			@RequestParam(value= "userId",required=false) Integer userId,
			@RequestParam(value= "userName",required=false) String userName,

			
			@RequestParam(value= "page",required=false) Integer page,
			@RequestParam(value="size",required=false) Integer size) {
		page = (page==null ? 0:page);
		size = (size==null ? 10:size);
		
		
		Pageable pageable= PageRequest.of(page, size,Sort.by(Direction.ASC,"id"));
		
		if( id != null ) {
			List<Bill> bills = billRepo.findAllById(Arrays.asList(id));
			
			model.addAttribute("count", bills.size());
			model.addAttribute("totalPages",1);
			model.addAttribute("billList",bills);
		}else{		
			Page<Bill> pageRS = null;
			
			if(start != null && end != null) {
				pageRS = billRepo.searchByDate(  start, end, pageable);						
			}else if (start != null) {
				pageRS = billRepo.searchByStartDate( start, pageable);	
			}else if (end != null) {
				pageRS = billRepo.searchByEndDate( end, pageable);
			}else if (StringUtils.hasText(userName)) {
				pageRS = billRepo.searchByUserName( "%" + userName + "%", pageable);				
			}else if(userId !=null) {
				pageRS = billRepo.searchByUserId(userId,pageable);
			}else {
				pageRS = billRepo.findAll(pageable);
						
			}

			model.addAttribute("count", pageRS.getTotalElements());
			model.addAttribute("totalPages", pageRS.getTotalPages());
			model.addAttribute("billList",pageRS.getContent());
		}
		
		model.addAttribute("id",id);
		model.addAttribute("userName",userName);
		model.addAttribute("userId",userId);
		
		model.addAttribute("size",size);
		model.addAttribute("page",page);
		
		model.addAttribute("userList",userRepo.findAll());

		return "bill/search";
	}
	@GetMapping("/edit")
	public String edit(Model model,@RequestParam("id")int id) {
		Bill bill = billRepo.findById(id).orElse(null);
		model.addAttribute("bill",bill);
		model.addAttribute("userList", userRepo.findAll());	

		return "bill/edit";
	}
	
	@PostMapping("/edit")
	public String edit(@ModelAttribute("bill") Bill bill) {
		billRepo.save(bill);
		return "redirect:/admin/bill/search";
	}
}
