package project3.Shoppee.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project3.Shoppee.Entity.Billitem;
import project3.Shoppee.Entity.Product;
import project3.Shoppee.Repository.ProductRepo;
@Controller
public class CartController {
	
	@Autowired
	ProductRepo productRepo;

	double total = 0;

	@GetMapping(value = { "/add-to-cart" })
	public String cart(HttpServletRequest request, @RequestParam(name = "id") int id, HttpSession session) {
		Product product = productRepo.getById(id);
		System.out.println(product.getName());

		if (session.getAttribute("cart") == null) {
			Billitem billitem = new Billitem();
			billitem.setQuantity(1);			
			billitem.setBuyPrice(product.getPrice());
			billitem.setProduct(product);

			Map<Integer,Billitem> map = new HashMap<Integer,Billitem>();
			map.put(id, billitem);
			//set vao session
			//hoac save db BillitemCart(userId)
			session.setAttribute("cart", map);
		} else {
			Map<Integer,Billitem> map = (Map<Integer,Billitem>) session.getAttribute("cart");
			Billitem billitem = map.get(id);
			if(billitem == null ) {
				billitem = new Billitem();
				billitem.setQuantity(1);			
				billitem.setBuyPrice(product.getPrice());
				billitem.setProduct(product);
				map.put(id, billitem);
				
			}else {
				billitem.setQuantity(billitem.getQuantity() +1);	
			}
			session.setAttribute("cart", map);

	}
		return "redirect:/view-cart";
	}
	
	@GetMapping("/view-cart" )
	public String cart(HttpServletRequest request, HttpSession session) {
		if (session.getAttribute("cart") != null) {
			Map<Integer, Billitem> map = (Map<Integer, Billitem>) session.getAttribute("cart");

			double total = 0;
			
			for (Entry<Integer, Billitem> entry : map.entrySet()) {
				total += (entry.getValue().getBuyPrice() * entry.getValue().getQuantity());
			}

			request.setAttribute("total", total);
		} else
			request.setAttribute("total", 0);

		return "cart/view-cart";
	}

		

	@GetMapping(value = { "/delete-cart" })
	public String delete(HttpServletRequest request, HttpSession session, @RequestParam(name = "id") int id) {

		if (session.getAttribute("cart") != null) {
			Map<Integer,Billitem> map = (Map<Integer,Billitem>) session.getAttribute("cart");
			
			 map.remove(id);
		}

			return "redirect:/view-cart";
		
	}
	@GetMapping(value = { "/clear-cart" })
	public String deletehome(HttpSession session) {
		session.removeAttribute("cart");
		return "redirect:/view-cart";
	}


}	
		




