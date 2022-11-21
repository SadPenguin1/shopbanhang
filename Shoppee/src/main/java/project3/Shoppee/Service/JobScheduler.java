//package project3.Shoppee.Service;
//
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import project3.Shoppee.Entity.Bill;
//import project3.Shoppee.Entity.User;
//import project3.Shoppee.Repository.BillRepo;
//import project3.Shoppee.Repository.UserRepo;
//
//@Component
//public class JobScheduler {
//	@Autowired
//	UserRepo userRepo;
//	
//	@Autowired
//	BillRepo billRepo;
//	
//	@Autowired
//	MailService mailService;
//	
//	
//	//@Scheduled(fixedDelay = 1000 * 60 * 5)
//	
//	public void sendEmail() {
//		
//	}
//	//QUARZT SCHEDULER
//	//Giay Phut Gio Ngay Thang Thu
//	@Scheduled(cron = "0 0 9 * * *")
//	public void sendemail() {
//		System.out.println("hello job");
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.HOUR_OF_DAY,0);
//		cal.set(Calendar.MINUTE,0);
//		cal.set(Calendar.SECOND,0);
//		cal.set(Calendar.MILLISECOND,0);
//		Date today = cal.getTime();
//
//
//		Page<User> users = userRepo.searchByBirthdate(today);
//		
//		for (User u : users) {
//			System.out.println(u.getName());
//			mailService.sendEmail("nguyenmanhdung01122000@gmail.com", "mail", "ưưưư");
//		}
//		
//	}
//		@Scheduled(fixedDelay = 1000 * 60 * 5)
//		//chi gio hien tai 5 phut
//		public void sendAdminemail() {
//			System.out.println("hello job");
//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.HOUR_OF_DAY,0);
//			
//			Date date = cal.getTime();
//
//
//			Page<Bill> bills = billRepo.searchByDateCreat(date);
//			
//			for (Bill b : bills) {
//				System.out.println(b.getId());
//			}
//		mailService.sendEmail("nguyenmanhdung01122000@gmail.com", "mail", "ưưưư");
//		
//		
//	}
//
//}
