package project3.Shoppee.Controller;

import java.security.Principal;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import project3.Shoppee.Entity.Bill;
import project3.Shoppee.Entity.Billitem;
import project3.Shoppee.Repository.BillRepo;
import project3.Shoppee.Repository.BillitemRepo;
import project3.Shoppee.Repository.UserRepo;

@Controller
public class PaymentController {
	
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private BillRepo billRepo;

	@Autowired
	private BillitemRepo billitemsRepo;


	@SuppressWarnings("unchecked")
	@GetMapping("/member/payment")
	public String payment(HttpSession session, Principal principal) {

		if (session.getAttribute("cart") != null) {
			Map<Integer, Billitem> map = (Map<Integer, Billitem>) session.getAttribute("cart");

			Bill bill = new Bill();
			bill.setUser(userRepo.findByEmail(principal.getName()));

			billRepo.save(bill);

			for (Entry<Integer, Billitem> entry : map.entrySet()) {
				Billitem billItem = entry.getValue();
				billItem.setBill(bill);
				billitemsRepo.save(billItem);
			}
		}
		return "redirect:payment-success.html";
	}

	@GetMapping("/member/bills")
	public String billuser(Model model, Principal principal) {
		model.addAttribute("bills", billRepo.findByUser(principal.getName()));//name la email xem lai loginservice
		return "user/bills.html";
	}

	

}
