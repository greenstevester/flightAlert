package net.greensill.flightalert.service;

import com.google.api.services.qpxExpress.model.LegInfo;
import com.google.api.services.qpxExpress.model.SegmentInfo;
import com.google.api.services.qpxExpress.model.SliceInfo;
import com.google.api.services.qpxExpress.model.TripOption;
import net.greensill.flightalert.config.JHipsterProperties;
import net.greensill.flightalert.domain.User;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private final String NL = System.lineSeparator();
    private final String TAB = "\t";

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Inject
    private JavaMailSender mailSender;

    @Inject
    private MessageSource messageSource;

    @Inject
    private SpringTemplateEngine templateEngine;

    private static Map<String,String> airlineCodeMap = new HashMap<>();

    static {
        airlineCodeMap.put("6A","AVIACSA");
        airlineCodeMap.put("9K","Cape Air");
        airlineCodeMap.put("A0","L'Avion");
        airlineCodeMap.put("A7","Air Plus Comet");
        airlineCodeMap.put("AA","American");
        airlineCodeMap.put("AC","Air Canada");
        airlineCodeMap.put("AF","Air France");
        airlineCodeMap.put("AI","Air India");
        airlineCodeMap.put("AM","Aeromexico");
        airlineCodeMap.put("AR","Aerolineas Argentinas");
        airlineCodeMap.put("AS","Alaska");
        airlineCodeMap.put("AT","Royal Air Maroc");
        airlineCodeMap.put("AV","Avianca");
        airlineCodeMap.put("AY","Finnair");
        airlineCodeMap.put("AZ","Alitalia");
        airlineCodeMap.put("B6","JetBlue");
        airlineCodeMap.put("BA","British Airways");
        airlineCodeMap.put("BD","bmi british midland");
        airlineCodeMap.put("BR","EVA Airways");
        airlineCodeMap.put("C6","CanJet Airlines");
        airlineCodeMap.put("CA","Air China");
        airlineCodeMap.put("CI","China");
        airlineCodeMap.put("CO","Continental");
        airlineCodeMap.put("CX","Cathay");
        airlineCodeMap.put("CZ","China Southern");
        airlineCodeMap.put("DL","Delta");
        airlineCodeMap.put("EI","Aer Lingus");
        airlineCodeMap.put("EK","Emirates");
        airlineCodeMap.put("EO","EOS");
        airlineCodeMap.put("F9","Frontier");
        airlineCodeMap.put("FI","Icelandair");
        airlineCodeMap.put("FJ","Air Pacific");
        airlineCodeMap.put("FL","AirTran");
        airlineCodeMap.put("G4","Allegiant");
        airlineCodeMap.put("GQ","Big Sky");
        airlineCodeMap.put("HA","Hawaiian");
        airlineCodeMap.put("HP","America West");
        airlineCodeMap.put("HQ","Harmony");
        airlineCodeMap.put("IB","Iberia");
        airlineCodeMap.put("JK","Spanair");
        airlineCodeMap.put("JL","JAL");
        airlineCodeMap.put("JM","Air Jamaica");
        airlineCodeMap.put("KE","Korean");
        airlineCodeMap.put("KU","Kuwait");
        airlineCodeMap.put("KX","Cayman");
        airlineCodeMap.put("LA","LanChile");
        airlineCodeMap.put("LH","Lufthansa");
        airlineCodeMap.put("LO","LOT");
        airlineCodeMap.put("LT","LTU");
        airlineCodeMap.put("LW","Pacific Wings");
        airlineCodeMap.put("LX","SWISS");
        airlineCodeMap.put("LY","El Al");
        airlineCodeMap.put("MA","MALEV");
        airlineCodeMap.put("MH","Malaysia");
        airlineCodeMap.put("MU","China Eastern");
        airlineCodeMap.put("MX","Mexicana");
        airlineCodeMap.put("NH","ANA");
        airlineCodeMap.put("NK","Spirit");
        airlineCodeMap.put("NW","Northwest");
        airlineCodeMap.put("NZ","Air New Zealand");
        airlineCodeMap.put("OS","Austrian");
        airlineCodeMap.put("OZ","Asiana");
        airlineCodeMap.put("PN","Pan American");
        airlineCodeMap.put("PR","Philippine");
        airlineCodeMap.put("QF","Qantas");
        airlineCodeMap.put("QK","Air Canada Jazz");
        airlineCodeMap.put("RG","VARIG");
        airlineCodeMap.put("SA","South African");
        airlineCodeMap.put("SK","SAS");
        airlineCodeMap.put("SN","SN Brussels");
        airlineCodeMap.put("SQ","Singapore");
        airlineCodeMap.put("SU","Aeroflot");
        airlineCodeMap.put("SY","Sun Country");
        airlineCodeMap.put("TA","Taca");
        airlineCodeMap.put("TG","Thai");
        airlineCodeMap.put("TK","Turkish");
        airlineCodeMap.put("TN","Air Tahiti Nui");
        airlineCodeMap.put("TP","TAP");
        airlineCodeMap.put("TS","Air Transat");
        airlineCodeMap.put("U5","USA 3000");
        airlineCodeMap.put("UA","United");
        airlineCodeMap.put("UP","Bahamasair");
        airlineCodeMap.put("US","US Air");
        airlineCodeMap.put("V3","Copa");
        airlineCodeMap.put("VS","Virgin Atlantic");
        airlineCodeMap.put("VX","Virgin America");
        airlineCodeMap.put("WA","Western");
        airlineCodeMap.put("WN","Southwest");
        airlineCodeMap.put("WS","WestJet");
        airlineCodeMap.put("XE","ExpressJet");
        airlineCodeMap.put("Y2","Globespan");
        airlineCodeMap.put("Y7","Silverjet");
        airlineCodeMap.put("YV","Mesa");
        airlineCodeMap.put("YX","Midwest");
        airlineCodeMap.put("ZK","Great Lake");
    }

    /**
     * System default email address that sends the e-mails.
     */
    private String from;

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {

        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            mailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendEmail(String to, String from, String subject, String content, boolean isMultipart, boolean isHtml) {

        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(from);

            // dumb down the subject heading for everyone.net seeing it chokes on anything complicated.
            if (from.equals("mailbot@greensill.net")) {
                message.setSubject("test");
            } else {
                message.setSubject(subject);
            }

            message.setText(content, isHtml);
            mailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }


    @Async
    public void sendActivationEmail(User user, String baseUrl) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("activationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendCreationEmail(User user, String baseUrl) {
        log.debug("Sending creation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("creationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendPasswordResetMail(User user, String baseUrl) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("passwordResetEmail", context);
        String subject = messageSource.getMessage("email.reset.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendSocialRegistrationValidationEmail(User user, String provider) {
        log.debug("Sending social registration validation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("provider", WordUtils.capitalize(provider));
        String content = templateEngine.process("socialRegistrationValidationEmail", context);
        String subject = messageSource.getMessage("email.social.registration.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }


    @Async
    public void sendFlightRequestResultEmail(User user, List<TripOption> tripResults, String emailSubject) {

        log.info("Sending FlightRequestResult e-mail...'");

        Locale locale = Locale.forLanguageTag(user.getLangKey());
        // comes from the messages_en.properties file
        StringBuilder emailSubjectPrefix = new StringBuilder(messageSource.getMessage("email.flightrequestresult.title", null, locale));

        StringBuilder emailContent = new StringBuilder().append(NL);
        StringBuilder exclusionCriteriaReasons = new StringBuilder("No results returned - due to following reasons:").append(NL);
        int optionCnt=1;
        boolean contentAdded = false;

        if (tripResults!=null) {

            for (TripOption tripOption : tripResults) {

                if (failsExclusionCriteria(tripOption, exclusionCriteriaReasons)) {
                    break;
                }

                StringBuilder header = new StringBuilder();
                header.append(NL)
                    .append("==================================================").append(NL)
                    .append("OPTION #").append(optionCnt).append(NL)
                    .append("Total Price: ").append(tripOption.getSaleTotal()).append(NL);

                int sliceCnt = 1;
                for (SliceInfo sliceInfo : tripOption.getSlice()) {

                    int totalFlyingTime = sliceInfo.getDuration() / 60;

                    if (sliceCnt == 1) {
                        emailContent.append(header)
                            .append("Total flying time for all legs (h): ").append(totalFlyingTime).append(NL)
                            .append("==================================================").append(NL).append(NL)
                            .append("=========== [OUTBOUND JOURNEY] =========== ").append(NL);

                    } else {
                        emailContent.append(NL).append("=========== [RETURN JOURNEY] ===========").append(NL);
                    }


                    emailContent
                        .append("Num segments: ")
                        .append(sliceInfo.getSegment().size())
                        .append(NL).append(NL);


                    int segCnt = 1;
                    for (SegmentInfo segInfo : sliceInfo.getSegment()) {

                        emailContent.append(NL)
                            .append("segment #").append(segCnt).append(NL)
                            .append("..........").append(NL)
                            .append("carrier: ").append(getAirlineFromCode(segInfo.getFlight().getCarrier())).append(NL)
                            .append("flight Number: ").append(segInfo.getFlight().getCarrier()).append(segInfo.getFlight().getNumber()).append(NL)
                            .append("cabin: ").append(segInfo.getCabin()).append(NL)
                            .append("connection waiting time (hrs): ").append(getDuration(segInfo.getConnectionDuration())).append(NL);

                        emailContent
                            .append(NL)
                            .append("Num legs: ")
                            .append(segInfo.getLeg().size()).append(NL);

                        int legCnt = 1;
                        for (LegInfo legInfo : segInfo.getLeg()) {

                            emailContent

                                .append("leg #").append(legCnt).append(NL)
                                .append("FROM:").append(legInfo.getOrigin()).append(" TO:").append(legInfo.getDestination()).append(NL)
                                .append("DEP:").append(legInfo.getDepartureTime()).append(" / ARR:").append(legInfo.getArrivalTime()).append(NL)
                                .append("flight time (h):").append(getDuration(legInfo.getDuration())).append(NL)
                                .append("aircaft Type: ").append(legInfo.getAircraft()).append(NL)
                                .append(NL);

                            legCnt++;
                            contentAdded = true;

                        }
                        segCnt++;
                    }
                    sliceCnt++;
                }
                optionCnt++;
            }
        }

        String mergedEmailSubject = emailSubjectPrefix + emailSubject;
        if (contentAdded) {
            log.info("email subject string:[{}]", mergedEmailSubject);
            // sendEmail(user.getEmail(), emailSubjectPrefix + emailSubject, emailContent.toString(), false, false);
            sendEmail("steve@greensill.net", jHipsterProperties.getMail().getFrom(), emailSubjectPrefix + emailSubject, emailContent.toString(), false, false);
        } else {
            log.info("email subject string:[{}]", mergedEmailSubject +" nothing found ");
            sendEmail("steve@greensill.net", jHipsterProperties.getMail().getFrom(), mergedEmailSubject +" reason: nothing found ", exclusionCriteriaReasons.toString(), false, false);
        }

    }

    private String getDuration(Integer connectionDuration) {

        if ( connectionDuration==null || connectionDuration==0 ) {
            return "none";
        }

        return String.valueOf(connectionDuration.intValue() / 60);
    }

    private String getAirlineFromCode(@NotNull String carrier) {

        if (airlineCodeMap.containsKey(carrier)) {
            return airlineCodeMap.get(carrier);
        }

        return "unknown";
    }

    private boolean failsExclusionCriteria(TripOption tripOption, StringBuilder exclusionCriteriaReasons) {

        int len = tripOption.getSaleTotal().length();
        Double saleTotal = Double.valueOf(tripOption.getSaleTotal().substring(3, len-1));
        if (saleTotal>6100) {
            exclusionCriteriaReasons.append("fail criteria:saleTotal>6100").append(" / actual saleTotal:").append(saleTotal).append(NL);
            return true;
        }

        SliceInfo sliceInfo = tripOption.getSlice().get(0);

        int totalFlyingTime = sliceInfo.getDuration()/60;

        if (totalFlyingTime>30) {
            exclusionCriteriaReasons.append("fail criteria:totalFlyingTime>24").append(" / actual totalFlyingTime:").append(totalFlyingTime).append(NL);
            return true;
        }

        return false;

    }


}
