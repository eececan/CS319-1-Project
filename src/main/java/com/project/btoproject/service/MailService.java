package com.project.btoproject.service;

import com.project.btoproject.enums.EventType;
import com.project.btoproject.enums.Hour;
import com.project.btoproject.model.Event;
import com.project.btoproject.model.IndividualTour;
import com.project.btoproject.model.Tour;
import com.project.btoproject.model.User;
import com.project.btoproject.repository.IAllUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class MailService {
    private JavaMailSender javaMailSender;
    private final IUserService userService;
    private final IAllUsersRepository allUsersRepository;

    @Autowired
    public MailService(JavaMailSender javaMailSender, @Lazy IUserService userService, IAllUsersRepository allUsersRepository) {
        this.javaMailSender = javaMailSender;
        this.userService = userService;
        this.allUsersRepository = allUsersRepository;
    }

    @Async
    public void sendApprovalMail(Event event) throws MailException, InterruptedException {
        if(event.getEventType().equals(EventType.TOUR)){
            System.out.println("Sending email...");
            Tour tour = (Tour)event;
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo("aycaatac88@gmail.com");
            //mail.setTo(tour.getSchoolCounselor().getEmail());
            mail.setFrom("dilekyildizbto@gmail.com");
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            mail.setSubject("Bilkent Üniversitesi tur başvurunuz kabul edildi!");
            mail.setText("Sayın " + tour.getSchoolCounselor().getName() + ",\n\nBilkent Üniversitesi turlarına katılmak için başvuru yaptığınız için teşekkür ederiz! " + formatter.format(tour.getDate()) + " tarihinde " + tour.getHour().toFormattedTime() + " saatindeki tur başvurunuz onaylanmıştır. Bilkent Üniversitesi Tanıtım Ofisi " + tour.getSchool().getName() + " öğrencilerini ağırlamak için heyecanlanıyor!\nGirilen forma göre turumuz " + tour.getPeopleCount() + " öğrenci içerecek ve " + (int) Math.ceil(tour.getPeopleCount() / 60.0) + " rehber içerecektir. Öğrenci sayısı veya tarih hakkında değişim yapmak için lütfen bu mail adresi üzerinden bizimle iletişime geçin!\n\n\n\n\nSaygılarımla,\nDilek Yıldız\nBilkent Ofisi Baş Sekreteri");
            javaMailSender.send(mail);
            System.out.println("Email Sent!");
        }
        /*if(event.getEventType().equals(EventType.INDIVIDUAL_TOUR)){
            System.out.println("Sending email...");
            IndividualTour tour = (IndividualTour)event;
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(tour.getContactEmail());
            mail.setFrom("dilekyildizbto@gmail.com");
            mail.setSubject("Bilkent Üniversitesi tur başvurunuz kabul edildi!");
            mail.setText("Sayın " + tour.getContactPerson() + ",\nBilkent Üniversitesi turlarına katılmak için başvuru yaptığınız için teşekkür ederiz! " + tour.getDate() + " tarihinde " + tour.getHour() + " saatindeki tur başvurunuz onaylanmıştır. Bilkent Üniversitesi Tanıtım Ofisi sizi ağırlamak için heyecanlanıyor!\nGirilen forma göre turumuz " + tour.getPeopleCount() + " öğrenci içerecek ve yaptığınız başvuruda belirttiğiniz ilgi alanlarınıza en uygun rehber tarafından yönlendirilecektir. Öğrenci sayısı, tarih veya ilgi alanınız hakkında değişim yapmak için lütfen bu mail adresi üzerinden bizimle iletişime geçin!\n\n\n\n\nSaygılarımla,\nDilek Yıldız\nBilkent Ofisi Baş Sekreteri");
            javaMailSender.send(mail);
            System.out.println("Email Sent!");
        }*/
    }

    @Async
    public void sendRejectionMail(Event event) throws MailException, InterruptedException {
        if(event.getEventType().equals(EventType.TOUR)){
            System.out.println("Sending email...");
            Tour tour = (Tour)event;
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo("aycaatac88@gmail.com");
            //mail.setTo(tour.getSchoolCounselor().getEmail());
            mail.setFrom("dilekyildizbto@gmail.com");
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            mail.setSubject("Bilkent Üniversitesi turu için yeni bir tarih seçin!");
            mail.setText("Sayın " + tour.getSchoolCounselor().getName() + ",\n\nBilkent Üniversitesi turlarına katılmak için başvuru yaptığınız için teşekkür ederiz! " +            formatter.format(tour.getDate())
                    + " tarihinde " + tour.getHour().toFormattedTime() + " saatindeki tur başvurunuz doluluk nedeniyle kabul edilememektedir. Ancak Bilkent Üniversitesi Tanıtım Ofisi " + tour.getSchool().getName() + " öğrencilerini ağırlamak için sabırsızlanıyor! Lütfen turunuz için yeni bir başvuru yapınız!\n" + "\n\n\n\n\nSaygılarımla,\nDilek Yıldız\nBilkent Ofisi Baş Sekreteri");
            javaMailSender.send(mail);
            System.out.println("Email Sent!");
        }
        /*if(event.getEventType().equals(EventType.INDIVIDUAL_TOUR)){
            System.out.println("Sending email...");
            IndividualTour tour = (IndividualTour)event;
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(tour.getContactEmail());
            mail.setFrom("dilekyildizbto@gmail.com");
            mail.setSubject("Bilkent Üniversitesi turu için yeni bir tarih seçin!");
            mail.setText("Sayın " + tour.getContactPerson() + ",\nBilkent Üniversitesi turlarına katılmak için başvuru yaptığınız için teşekkür ederiz! " + tour.getDate() + " tarihinde " + tour.getHour() + " saatindeki tur başvurunuz doluluk nedeniyle kabul edilememektedir. Ancak Bilkent Üniversitesi Tanıtım Ofisi sizi ağırlamak için sabırsızlanıyor!Lütfen turunuz için yeni bir başvuru yapınız!\n" + "\n\n\n\n\nSaygılarımla,\nDilek Yıldız\nBilkent Ofisi Baş Sekreteri");
            javaMailSender.send(mail);
            System.out.println("Email Sent!");
        }*/
    }

    @Async
    public void sendForgotPasswordMail(String email, String password) throws MailException, InterruptedException {
            System.out.println("Sending email...");
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(email);
            mail.setFrom("dilekyildizbto@gmail.com");
            mail.setSubject("You can login with your new password!");
            mail.setText("Your new password is set as: " + password + "\n You can change your password once you log in! \n\n\nHave a good day!");
            User user = allUsersRepository.findUserByEmail(email);
            userService.changePassword(user.getId(), password);
            javaMailSender.send(mail);
            System.out.println("Email Sent!");
        /*if(event.getEventType().equals(EventType.INDIVIDUAL_TOUR)){
            System.out.println("Sending email...");
            IndividualTour tour = (IndividualTour)event;
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(tour.getContactEmail());
            mail.setFrom("dilekyildizbto@gmail.com");
            mail.setSubject("Bilkent Üniversitesi tur başvurunuz kabul edildi!");
            mail.setText("Sayın " + tour.getContactPerson() + ",\nBilkent Üniversitesi turlarına katılmak için başvuru yaptığınız için teşekkür ederiz! " + tour.getDate() + " tarihinde " + tour.getHour() + " saatindeki tur başvurunuz onaylanmıştır. Bilkent Üniversitesi Tanıtım Ofisi sizi ağırlamak için heyecanlanıyor!\nGirilen forma göre turumuz " + tour.getPeopleCount() + " öğrenci içerecek ve yaptığınız başvuruda belirttiğiniz ilgi alanlarınıza en uygun rehber tarafından yönlendirilecektir. Öğrenci sayısı, tarih veya ilgi alanınız hakkında değişim yapmak için lütfen bu mail adresi üzerinden bizimle iletişime geçin!\n\n\n\n\nSaygılarımla,\nDilek Yıldız\nBilkent Ofisi Baş Sekreteri");
            javaMailSender.send(mail);
            System.out.println("Email Sent!");
        }*/
    }
}

