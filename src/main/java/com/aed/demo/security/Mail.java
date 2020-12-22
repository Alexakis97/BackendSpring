package com.aed.demo.security;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.aed.demo.entity.Ambulance;
import com.aed.demo.entity.User;

public class Mail {

    private static String USER_NAME = "nicksterg9"; // GMail user name (just the part before "@gmail.com")
    private static String PASSWORD = "gbxxfgfhujdfqndd"; // GMail password
    // private static String RECIPIENT = "gster@compulaw.com";

    public boolean sendEmailOKMail(String RECIPIENT, String sub, String title, String body, String under_line_text,
            String end_text) {

        String from = USER_NAME;
        String pass = PASSWORD;
        String[] to = { RECIPIENT }; // list of recipient email addresses
        String subject = sub;
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for (int i = 0; i < to.length; i++) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for (int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "<div style=\" background-color: white;width: 25vw;height:auto;border: 20px solid grey;padding: 50px;margin:100 auto;\">\n"
                    + "<h1 style=\"text-align: center;font-size:1.5vw\">" + title + "</h1>\n" + "<div align=\"center\">"
                    + "<h2 style=\"text-align: center;font-size:1.0vw\">" + body + "</h2>" +

                    "<h3 style=\"text-align: center;text-decoration: underline;text-decoration-color: red;font-size:0.9vw\">"
                    + under_line_text + "</h3><br><h4 style=\"text-align: center;font-size:0.7vw\">" + end_text
                    + " </h4></div>";
            messageBodyPart.setContent(htmlText, "text/html");

            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
        return true;
    }

    public boolean sendEmail(String RECIPIENT, String sub, String title, String body, String under_line_text,
            String end_text) {
        String from = USER_NAME;
        String pass = PASSWORD;
        String[] to = { RECIPIENT }; // list of recipient email addresses
        String subject = sub;
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for (int i = 0; i < to.length; i++) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for (int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "<div style=\" background-color: white;width: 25vw;height:auto;border: 20px solid grey;padding: 50px;margin:100 auto;\">\n"
                    + "<h1 style=\"text-align: center;font-size:1.5vw\">" + title + "</h1>\n" + "<div align=\"center\">"
                    + "<h2 style=\"text-align: center;font-size:1.0vw\">" + body + "</h2>" +

                    "<h3 style=\"text-align: center;text-decoration: underline;text-decoration-color: red;font-size:0.9vw\">"
                    + under_line_text + "</h3><br><h4 style=\"text-align: center;font-size:0.7vw\">" + end_text
                    + " </h4></div>";
            messageBodyPart.setContent(htmlText, "text/html");

            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
        return true;
    }

    public boolean sendDefaultCredentialsMail(String RECIPIENT, String sub, String clinicName,
            ArrayList<User> userLicense, ArrayList<Ambulance> ambulanceLicense, ArrayList<User> masterClinicLicense) {

        String username = "sales-support@foreverlifetime.com";
        String pass = "m:y3hN3/KKb2JpU7HHm879X9PZW6FNy=";
        String from = "sales@foreverlifetime.com";
        String[] to = { RECIPIENT }; // list of recipient email addresses
        String subject = sub;
        Properties props = System.getProperties();
        String host = "smtp.zoho.eu";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.from", from);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(username));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for (int i = 0; i < to.length; i++) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for (int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            BodyPart messageBodyPart = new MimeBodyPart();

            StringBuilder htmlText = new StringBuilder();
            htmlText.append("<!DOCTYPE html>").append(
                    "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">")
                    .append("<head>").append("    <meta charset=\"utf-8\"> <!-- utf-8 works for most cases -->")
                    .append("    <meta name=\"viewport\" content=\"width=device-width\"> <!-- Forcing initial-scale shouldn't be necessary -->")
                    .append("    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> <!-- Use the latest (edge) version of IE rendering engine -->")
                    .append("    <meta name=\"x-apple-disable-message-reformatting\">  <!-- Disable auto-scale in iOS 10 Mail entirely -->")
                    .append("    <title></title> <!-- The title tag shows in email notifications, like Android 4.4. -->")
                    .append("")
                    .append("    <link href=\"https://fonts.googleapis.com/css?family=Work+Sans:200,300,400,500,600,700\" rel=\"stylesheet\">")
                    .append("").append("    <!-- CSS Reset : BEGIN -->").append("    <style>").append("")
                    .append("        /* What it does: Remove spaces around the email design added by some email clients. */")
                    .append("        /* Beware: It can remove the padding / margin and add a background color to the compose a reply window. */")
                    .append("        html,").append("body {").append("    margin: 0 auto !important;")
                    .append("    padding: 0 !important;").append("    height: 100% !important;")
                    .append("    width: 100% !important;").append("    background: #f1f1f1;").append("}").append("")
                    .append("/* What it does: Stops email clients resizing small text. */").append("* {")
                    .append("    -ms-text-size-adjust: 100%;").append("    -webkit-text-size-adjust: 100%;").append("}")
                    .append("").append("/* What it does: Centers email on Android 4.4 */")
                    .append("div[style*=\"margin: 16px 0\"] {").append("    margin: 0 !important;").append("}")
                    .append("").append("/* What it does: Stops Outlook from adding extra spacing to tables. */")
                    .append("table,").append("td {").append("    mso-table-lspace: 0pt !important;")
                    .append("    mso-table-rspace: 0pt !important;").append("}").append("")
                    .append("/* What it does: Fixes webkit padding issue. */").append("table {")
                    .append("    border-spacing: 0 !important;").append("    border-collapse: collapse !important;")
                    .append("    table-layout: fixed !important;").append("    margin: 0 auto !important;").append("}")
                    .append("").append("/* What it does: Uses a better rendering method when resizing images in IE. */")
                    .append("img {").append("    -ms-interpolation-mode:bicubic;").append("}").append("")
                    .append("/* What it does: Prevents Windows 10 Mail from underlining links despite inline CSS. Styles for underlined links should be inline. */")
                    .append("a {").append("    text-decoration: none;").append("}").append("")
                    .append("/* What it does: A work-around for email clients meddling in triggered links. */")
                    .append("*[x-apple-data-detectors],  /* iOS */").append(".unstyle-auto-detected-links *,")
                    .append(".aBn {").append("    border-bottom: 0 !important;")
                    .append("    cursor: default !important;").append("    color: inherit !important;")
                    .append("    text-decoration: none !important;").append("    font-size: inherit !important;")
                    .append("    font-family: inherit !important;").append("    font-weight: inherit !important;")
                    .append("    line-height: inherit !important;").append("}").append("")
                    .append("/* What it does: Prevents Gmail from displaying a download button on large, non-linked images. */")
                    .append(".a6S {").append("    display: none !important;").append("    opacity: 0.01 !important;")
                    .append("}").append("")
                    .append("/* What it does: Prevents Gmail from changing the text color in conversation threads. */")
                    .append(".im {").append("    color: inherit !important;").append("}").append("")
                    .append("/* If the above doesn't work, add a .g-img class to any image in question. */")
                    .append("img.g-img + div {").append("    display: none !important;").append("}").append("")
                    .append("/* What it does: Removes right gutter in Gmail iOS app: https://github.com/TedGoas/Cerberus/issues/89  */")
                    .append("/* Create one of these media queries for each additional viewport size you'd like to fix */")
                    .append("").append("/* iPhone 4, 4S, 5, 5S, 5C, and 5SE */")
                    .append("@media only screen and (min-device-width: 320px) and (max-device-width: 374px) {")
                    .append("    u ~ div .email-container {").append("        min-width: 320px !important;")
                    .append("    }").append("}").append("/* iPhone 6, 6S, 7, 8, and X */")
                    .append("@media only screen and (min-device-width: 375px) and (max-device-width: 413px) {")
                    .append("    u ~ div .email-container {").append("        min-width: 375px !important;")
                    .append("    }").append("}").append("/* iPhone 6+, 7+, and 8+ */")
                    .append("@media only screen and (min-device-width: 414px) {")
                    .append("    u ~ div .email-container {").append("        min-width: 414px !important;")
                    .append("    }").append("}").append("    </style>").append("")
                    .append("    <!-- CSS Reset : END -->").append("")
                    .append("    <!-- Progressive Enhancements : BEGIN -->").append("    <style>").append("")
                    .append("	    .primary{").append("	background: #17bebb;").append("}").append(".bg_white{")
                    .append("	background: #ffffff;").append("}").append(".bg_light{")
                    .append("	background: #f7fafa;").append("}").append(".bg_black{")
                    .append("	background: #000000;").append("}").append(".bg_dark{")
                    .append("	background: rgba(0,0,0,.8);").append("}").append(".email-section{")
                    .append("	padding:2.5em;").append("}").append("").append("/*BUTTON*/").append(".btn{")
                    .append("	padding: 10px 15px;").append("	display: inline-block;").append("}")
                    .append(".btn.btn-primary{").append("	border-radius: 5px;").append("	background: #17bebb;")
                    .append("	color: #ffffff;").append("}").append(".btn.btn-white{")
                    .append("	border-radius: 5px;").append("	background: #ffffff;").append("	color: #000000;")
                    .append("}").append(".btn.btn-white-outline{").append("	border-radius: 5px;")
                    .append("	background: transparent;").append("	border: 1px solid #fff;").append("	color: #fff;")
                    .append("}").append(".btn.btn-black-outline{").append("	border-radius: 0px;")
                    .append("	background: transparent;").append("	border: 2px solid #000;").append("	color: #000;")
                    .append("	font-weight: 700;").append("}").append(".btn-custom{").append("	color: rgba(0,0,0,.3);")
                    .append("	text-decoration: underline;").append("}").append("").append("h1,h2,h3,h4,h5,h6{")
                    .append("	font-family: 'Work Sans', sans-serif;").append("	color: #000000;")
                    .append("	margin-top: 0;").append("	font-weight: 400;").append("}").append("").append("body{")
                    .append("	font-family: 'Work Sans', sans-serif;").append("	font-weight: 400;")
                    .append("	font-size: 15px;").append("	line-height: 1.8;").append("	color: rgba(0,0,0,.4);")
                    .append("}").append("").append("a{").append("	color: #17bebb;").append("}").append("")
                    .append("table{").append("}").append("/*LOGO*/").append("").append(".logo h1{")
                    .append("	margin: 0;").append("}").append(".logo h1 a{").append("	color: #c13b4d;")
                    .append("	font-size: 24px;").append("	font-weight: 700;")
                    .append("	font-family: 'Work Sans', sans-serif;").append("}").append("").append("/*HERO*/")
                    .append(".hero{").append("	position: relative;").append("	z-index: 0;").append("}").append("")
                    .append(".hero .text{").append("	color: rgba(0,0,0,.3);").append("}").append(".hero .text h2{")
                    .append("	color: #000;").append("	font-size: 34px;").append("	margin-bottom: 15px;")
                    .append("	font-weight: 300;").append("	line-height: 1.2;").append("}")
                    .append(".hero .text h3{").append("	font-size: 24px;").append("	font-weight: 200;").append("}")
                    .append(".hero .text h2 span{").append("	font-weight: 600;").append("	color: #000;")
                    .append("}").append("").append("").append("/*PRODUCT*/").append(".product-entry{")
                    .append("	display: block;").append("	position: relative;").append("	float: left;")
                    .append("	padding-top: 20px;").append("}").append(".product-entry .text{")
                    .append("	width: calc(100% - 125px);").append("	padding-left: 20px;").append("}")
                    .append(".product-entry .text h3{").append("	margin-bottom: 0;").append("	padding-bottom: 0;")
                    .append("}").append(".product-entry .text p{").append("	margin-top: 0;").append("}")
                    .append(".product-entry img, .product-entry .text{").append("	float: left;").append("}")
                    .append("").append("ul.social{").append("	padding: 0;").append("}").append("ul.social li{")
                    .append("	display: inline-block;").append("	margin-right: 10px;").append("}").append("")
                    .append("/*FOOTER*/").append("").append(".footer{")
                    .append("	border-top: 1px solid rgba(0,0,0,.05);").append("	color: rgba(0,0,0,.5);").append("}")
                    .append(".footer .heading{").append("	color: #000;").append("	font-size: 20px;").append("}")
                    .append(".footer ul{").append("	margin: 0;").append("	padding: 0;").append("}")
                    .append(".footer ul li{").append("	list-style: none;").append("	margin-bottom: 10px;")
                    .append("}").append(".footer ul li a{").append("	color: rgba(0,0,0,1);").append("}").append("")
                    .append("").append("@media screen and (max-width: 500px) {").append("").append("").append("}")
                    .append("").append("").append("    </style>").append("").append("").append("</head>").append("")
                    .append("<body width=\"100%\" style=\"margin: 0; padding: 0 !important; mso-line-height-rule: exactly; background-color: #f1f1f1;\">")
                    .append("	<center style=\"width: 100%; background-color: #f1f1f1;\">")
                    .append("    <div style=\"display: none; font-size: 1px;max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden; mso-hide: all; font-family: sans-serif;\">")
                    .append(" ").append("    </div>")
                    .append("    <div style=\"max-width: 600px; margin: 0 auto;\" class=\"email-container\">")
                    .append("    	<!-- BEGIN BODY -->")
                    .append("      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">")
                    .append("      	<tr>")
                    .append("          <td valign=\"top\" class=\"bg_white\" style=\"padding: 1em 2.5em 0 2.5em;\">")
                    .append("          	<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">")
                    .append("          		<tr>")
                    .append("          			<td class=\"logo\" style=\"text-align: left;\">")
                    .append("			            <h1><a href=\"https://foreverlifetime.com/\">Lifetime Health Software Solutions</a></h1>")
                    .append("			          </td>").append("          		</tr>")
                    .append("          	</table> ").append("          </td>").append("	      </tr><!-- end tr -->")
                    .append("				<tr>")
                    .append("          <td valign=\"middle\" class=\"hero bg_white\" style=\"padding: 2em 0 2em 0;\">")
                    .append("            <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">")
                    .append("            	<tr>")
                    .append("            		<td style=\"padding: 0 2.5em; text-align: left;\">")
                    .append("            			<div class=\"text\">")
                    .append("            				<h2> Purchased service licenses of " + clinicName + "</h2>")
                    .append("            				<h3>User, ambulance and master licenses default credentials for your purcahsed users</h3>")
                    .append("            			</div>").append("            		</td>")
                    .append("            	</tr>").append("            </table>").append("          </td>")
                    .append("	      </tr><!-- end tr -->").append("	      <tr> </table> ")

                    .append("<br> <br>	      	<table class=\"bg_white\" role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">")
                    .append("	      		<tr style=\"border-bottom: 1px solid rgba(0,0,0,.05);\">")
                    .append("					    <th width=\"80%\" style=\"text-align:left; padding: 0 2.5em; color: #000; padding-bottom: 20px\">User Licenses</th>")

                    .append("					  </tr>");

            int random = (int) (Math.random() * 15 + 1);

            System.out.println("RANDOM NUMBER  = " + random);

            for (User user : userLicense) {
                htmlText.append("			<tr style=\"border-bottom: 1px solid rgba(0,0,0,.05);\">").append(
                        "					  	<td valign=\"middle\" width=\"80%\" style=\"text-align:left; padding: 0 2.5em;\">")
                        .append("					  		<div class=\"product-entry\">")
                        .append("					  			<img src=\"../../../../resources/static/img/avatar-"
                                + random
                                + ".png\" alt=\"\" style=\"width: 100px; max-width: 600px; height: auto; margin-bottom: 20px; display: block;\">")
                        .append("					  			<div class=\"text\">")
                        .append("					  				<h3><strong>Username:</strong>  "
                                + user.getUsername() + "</h3>")
                        .append("					  			<br>")
                        .append("					  				<h3><strong>Password:</strong>  "
                                + user.getPassword() + "</h3>")
                        .append("					  			</div>").append("					  		</div>")
                        .append("					  	</td>")
                        .append("					  	<td valign=\"middle\" width=\"20%\" style=\"text-align:left; padding: 0 2.5em;\">")

                        .append("					  	</td>").append("					  </tr>");
            }

            htmlText.append("          	</table> <br> <br>");

            htmlText.append(
                    "	      	<table class=\"bg_white\" role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">")
                    .append("	      		<tr style=\"border-bottom: 1px solid rgba(0,0,0,.05);\">")
                    .append("					    <th width=\"80%\" style=\"text-align:left; padding: 0 2.5em; color: #000; padding-bottom: 20px\">Ambulance Licenses</th>")

                    .append("					  </tr>");

            random = (int) (Math.random() * 15 + 1);

            System.out.println("RANDOM NUMBER  = " + random);

            for (Ambulance ambulance : ambulanceLicense) {
                htmlText.append("					  <tr style=\"border-bottom: 1px solid rgba(0,0,0,.05);\">").append(
                        "					  	<td valign=\"middle\" width=\"80%\" style=\"text-align:left; padding: 0 2.5em;\">")
                        .append("					  		<div class=\"product-entry\">")
                        .append("					  			<img src=\"../../../../../../resources/static/img/avatar-"
                                + random
                                + ".png\" alt=\"\" style=\"width: 100px; max-width: 600px; height: auto; margin-bottom: 20px; display: block;\">")
                        .append("					  			<div class=\"text\">")
                        .append("					  				<h3><strong>Username:</strong> "
                                + ambulance.getUsername() + "</h3>")
                        .append("					  			<br>")
                        .append("					  				<h3><strong>Password:</strong> "
                                + ambulance.getPassword() + "</h3>")
                        .append("					  			</div>").append("					  		</div>")
                        .append("					  	</td>")
                        .append("					  	<td valign=\"middle\" width=\"20%\" style=\"text-align:left; padding: 0 2.5em;\">")

                        .append("					  	</td>").append("					  </tr>");
            }

            htmlText.append("          	</table> <br> <br>");

            htmlText.append(
                    "	      	<table class=\"bg_white\" role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">")
                    .append("	      		<tr style=\"border-bottom: 1px solid rgba(0,0,0,.05);\">")
                    .append("					    <th width=\"80%\" style=\"text-align:left; padding: 0 2.5em; color: #000; padding-bottom: 20px\">Master Licenses</th>")

                    .append("					  </tr>");

            random = (int) (Math.random() * 15 + 1);

            System.out.println("RANDOM NUMBER  = " + random);

            for (User user : masterClinicLicense) {
                htmlText.append("					  <tr style=\"border-bottom: 1px solid rgba(0,0,0,.05);\">").append(
                        "					  	<td valign=\"middle\" width=\"80%\" style=\"text-align:left; padding: 0 2.5em;\">")
                        .append("					  		<div class=\"product-entry\">")
                        .append("					  			<img src=\"../../../../../resources/static/img/avatar-"
                                + random
                                + ".png\" alt=\"\" style=\"width: 100px; max-width: 600px; height: auto; margin-bottom: 20px; display: block;\">")
                        .append("					  			<div class=\"text\">")
                        .append("					  				<h3><strong>Username:</strong>  "
                                + user.getUsername() + "</h3>")
                        .append("					  			<br>")
                        .append("					  				<h3><strong>Password:</strong>  "
                                + user.getPassword() + "</h3>")
                        .append("					  			</div>").append("					  		</div>")
                        .append("					  	</td>")
                        .append("					  	<td valign=\"middle\" width=\"20%\" style=\"text-align:left; padding: 0 2.5em;\">")

                        .append("					  	</td>").append("					  </tr>");
            }

            htmlText.append("          	</table> <br> <br>");

            htmlText.append(
                    "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">")
                    .append("      	<tr>")
                    .append("          <td valign=\"middle\" class=\"bg_light footer email-section\">")
                    .append("            <table>").append("            	<tr>")
                    .append("                <td valign=\"top\" width=\"33.333%\" style=\"padding-top: 20px;\">")
                    .append("                  <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">")
                    .append("                    <tr>")
                    .append("                      <td style=\"text-align: left; padding-right: 10px;\">")
                    .append("                      	<h3 class=\"heading\">About</h3>")
                    .append("                      	<p>Lifetime Health Software Solutions Service</p>")
                    .append("                      </td>").append("                    </tr>")
                    .append("                  </table>").append("                </td>")
                    .append("                <td valign=\"top\" width=\"33.333%\" style=\"padding-top: 20px;\">")
                    .append("                  <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">")
                    .append("                    <tr>")
                    .append("                      <td style=\"text-align: left; padding-left: 5px; padding-right: 5px;\">")
                    .append("                      	<h3 class=\"heading\">Contact Info</h3>")
                    .append("                      	<ul>")
                    .append("					                <li><span class=\"text\">Address: 55131 Kalamaria, Thessaloniki, Greece</span></li>")
                    .append("					                <li><span class=\"text\">Tel: +30 2310-245663</span></a></li>")
                    .append("					                <li><span class=\"text\">email: support@foreverlifetime.com</span></a></li>")
                    .append("					              </ul>").append("                      </td>")
                    .append("                    </tr>").append("                  </table>")
                    .append("                </td>")
                    .append("                <td valign=\"top\" width=\"33.333%\" style=\"padding-top: 20px;\">")
                    .append("                  <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">")
                    .append("                    <tr>")
                    .append("                      <td style=\"text-align: left; padding-left: 10px;\">")
                    .append("                      	<h3 class=\"heading\">Useful Links</h3>")
                    .append("                      	<ul>")
                    .append("					                <li><a href=\"https://lifetimeapplication.com/login\">Dashboard</a></li>")
                    .append("					                <li><a href=\"https://foreverlifetime.com/\">Website</a></li>")
                    .append("					                <li><a href=\"https://foreverlifetime.com/#contact\">Contact</a></li>")
                    .append("					              </ul>").append("                      </td>")
                    .append("                    </tr>").append("                  </table>")
                    .append("                </td>").append("              </tr>").append("            </table>")
                    .append("          </td>").append("        </tr><!-- end: tr -->").append("        <tr>")
                    .append("          <td class=\"bg_white\" style=\"text-align: center;\">")
                    .append("          	<p><a href=\"https://foreverlifetime.com/\" style=\"color: rgba(0,0,0,.8);\">Lifetime Health Software Solutions</a></p>")
                    .append("          </td>").append("        </tr>").append("      </table>").append("")
                    .append("    </div>").append("  </center>").append("</body>").append("</html>");

            messageBodyPart.setContent(htmlText.toString(), "text/html");

            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect(host, username, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
        return true;
    }

}