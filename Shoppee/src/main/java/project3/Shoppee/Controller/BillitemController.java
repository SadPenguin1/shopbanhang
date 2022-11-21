package project3.Shoppee.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
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

import project3.Shoppee.Entity.Billitem;
import project3.Shoppee.Repository.BillRepo;
import project3.Shoppee.Repository.BillitemRepo;
import project3.Shoppee.Repository.ProductRepo;
import project3.Shoppee.Repository.UserRepo;

@Controller
@RequestMapping("/admin/billitem")
public class BillitemController {
	@Autowired
	ProductRepo productRepo;
	@Autowired
	BillRepo billRepo;
	@Autowired
	BillitemRepo billitemRepo;


	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("billList", billRepo.findAll());
		model.addAttribute("productList" , productRepo.findAll());
		return "billitem/add";
	}
  
	@PostMapping("/add")
	public String add(@ModelAttribute @Valid Billitem billitem , BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "billitem/add";
		}
		billitemRepo.save(billitem);
		return "redirect:/admin/billitem/search";			
	}
		
	@GetMapping("/delete")
	public String delete(Model model,@Param("id") int id) {
		billitemRepo.deleteById(id);
		return "redirect:/admin/billitem/search";		
	}
	@GetMapping("/search")
	public String search(Model model,
			@RequestParam(value = "id",required=false) Integer id,
			@RequestParam(value= "buyPrice",required=false) Double buyPrice,	
			@RequestParam(value= "billId",required=false) Integer billId,	
			@RequestParam(value= "productId",required=false) Integer productId,
			@RequestParam(value= "productName",required=false) Integer productName,	

			
			@RequestParam(value= "page",required=false) Integer page,
			@RequestParam(value="size",required=false) Integer size) {
		page = (page==null ? 0:page);
		size = (size==null ? 10:size);
		
		Pageable pageable= PageRequest.of(page, size,Sort.by(Direction.ASC,"id"));
	
		if( id != null ) {
			List<Billitem> billitems = billitemRepo.findAllById(Arrays.asList(id));
			
			model.addAttribute("count", billitems.size());
			model.addAttribute("totalPages",1);
			model.addAttribute("billitemList",billitems);
		}else{		
			Page<Billitem> pageRS = null;
				pageRS = billitemRepo.findAll(pageable);
			

			model.addAttribute("count", pageRS.getTotalElements());
			model.addAttribute("totalPages", pageRS.getTotalPages());
			model.addAttribute("billitemList",pageRS.getContent());
		}

		model.addAttribute("id",id);
		model.addAttribute("buyPrice",buyPrice);	
		model.addAttribute("billId",billId);
		model.addAttribute("productId",productId);
	
		
		
		model.addAttribute("size",size);
		model.addAttribute("page",page);
		
		model.addAttribute("productList",billitemRepo.findAll());
		model.addAttribute("billList",billitemRepo.findAll());


		return "billitem/search";
	} 
	@GetMapping("/edit")
	public String edit(Model model,@RequestParam("id")int id) {
		Billitem billitem = billitemRepo.findById(id).orElse(null);
		model.addAttribute("billitem",billitem);
		model.addAttribute("billList",billRepo.findAll());
		model.addAttribute("productList",productRepo.findAll());
		return "billitem/edit";
	}
	
	@PostMapping("/edit")
	public String edit(@ModelAttribute("score") Billitem billitem) {
		billitemRepo.save(billitem);
		return "redirect:/admin/billitem/search";
	}
}
