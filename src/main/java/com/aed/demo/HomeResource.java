package com.aed.demo;

import static java.nio.charset.StandardCharsets.*;

import java.nio.charset.StandardCharsets;

import com.aed.demo.cluster.InitServiceCluster;
import com.aed.demo.cluster.Point;
import com.aed.demo.entity.*;
import com.aed.demo.fileupload.FileStorageServiceAedImages;
import com.aed.demo.fileupload.FileStorageServiceMobileImages;
import com.aed.demo.processor.AmbulanceProcessor;
import com.aed.demo.processor.AsirmatistisProcessor;
import com.aed.demo.processor.AssignProcessor;
import com.aed.demo.processor.CompletedProcessor;
import com.aed.demo.processor.NotificationProcessor;
import com.aed.demo.processor.Agios_Loukas_Processor;
import com.aed.demo.processor.Iaso_Thessalias_Processor;
import com.aed.demo.processor.MarketingProcessor;
import com.aed.demo.processor.UserProcessor;
import com.aed.demo.repositories.AEDRepository;
import com.aed.demo.repositories.AmbulanceRepository;
import com.aed.demo.repositories.EventRepository;
import com.aed.demo.repositories.HospitalsRepository;
import com.aed.demo.repositories.MarketingRepository;
import com.aed.demo.repositories.ReportsRepository;
import com.aed.demo.repositories.SmsRepository;
import com.aed.demo.repositories.TempUserRepository;
import com.aed.demo.repositories.UserRepository;
import com.aed.demo.security.AES;
import com.aed.demo.security.AuthenticationRequest;
import com.aed.demo.security.CustomPasswordEncoder;
import com.aed.demo.security.JwtUtil;
import com.aed.demo.security.MyUserDetailsService;

import reactor.core.publisher.Flux;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class HomeResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    public static final String DOMAIN = "https://lifetimeapplication.com";

    @Autowired
    UserRepository userRepo;

    @Autowired
    AEDRepository aedRepo;

    @Autowired
    EventRepository eventRepo;

    @Autowired
    MarketingRepository marketingRepo;

    @Autowired
    FileStorageServiceAedImages fileStorageServiceAed;

    @Autowired
    FileStorageServiceMobileImages fileStorageServiceMobile;

    @Autowired
    ReportsRepository reportsRepo;

    @Autowired
    TempUserRepository tempUserRepo;

    @Autowired
    SmsRepository smsRepository;

    @Autowired
    AmbulanceRepository ambulanceRepo;

    @Autowired
    HospitalsRepository hospitalRepo;

    @Autowired
    private AmbulanceProcessor ambulanceProcessor;

    @Autowired
    private AssignProcessor assignProcessor;

    @Autowired
    private AsirmatistisProcessor asirmatistisProcessor;

    @Autowired
    private CompletedProcessor completedProcessor;

    @Autowired
    private InitServiceCluster serviceCluster;

    @Autowired
    private Agios_Loukas_Processor agios_loukas_processor;

    @Autowired
    private Iaso_Thessalias_Processor iaso_thessalias_processor;

    @Autowired
    private NotificationProcessor notificationProcessor;

    @Autowired
    private UserProcessor userProcessor;

    @Autowired
    private MarketingProcessor marketingProcessor;

    // -----------------------------------JWT Auth----------------------------------
    @RequestMapping(value = "/authenticate/android", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {

        try {

            // String userpwFromDBString=
            // userDetailsService.loadUserByUsername(authenticationRequest.getUsername()).getPassword();

            // if(encoder.matches(authenticationRequest.getPassword(),userpwFromDBString ))
            // {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            // }else {
            // throw new BadCredentialsException("Bad Credentials");
            // }
            //
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(403, "Access Denied"), HttpStatus.FORBIDDEN);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);
        Optional<User> userOptional = userRepo.findByUsername(authenticationRequest.getUsername());
        User userFound = userOptional.get();

        if (userFound.getLat() != null && userFound.getLon() != null) {

            try {
                String municipality = serviceCluster
                        .returnClusterValueDimoi(new Point(userFound.getLat(), userFound.getLon()), "en");
                byte[] ptext = municipality.getBytes(ISO_8859_1);
                String municipalityText = new String(ptext, StandardCharsets.UTF_8);
                System.out.println(municipalityText);

                userFound.setMunicipality(convert(municipalityText));

                String nomos = serviceCluster
                        .returnClusterValueNomoi(new Point((userFound.getLat()), (userFound.getLon())), "en");
                userFound.setDepartment(nomos);

                String periferia = serviceCluster
                        .returnClusterValue(new Point((userFound.getLat()), (userFound.getLon())), "en");
                userFound.setPeriphery(periferia);
                userRepo.saveAndFlush(userFound);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        return new ResponseEntity<>(new ApiResponse(200, jwt, userFound), HttpStatus.OK);

    }

    // ------------------------------- USER -----------------------------------

    @GetMapping("/users")
    public List<User> users() {
        return userRepo.findAll();
    }

    @GetMapping("/users/count")
    public int countUsers() {

        List<User> userList = userRepo.findAll();
        return userList.size();

    }

    @GetMapping("/users/{userId}")
    public Optional<User> getUser(@PathVariable int userId) {

        return userRepo.findById(userId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/users/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable int userId) {
        userRepo.deleteById(userId);
        return new ResponseEntity<>(new ApiResponse(200, "User deleted"), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/users/update")
    public ResponseEntity<ApiResponse> addUser(@RequestBody User user) {

        Optional<User> userOptional = userRepo.findByUsername(user.getUsername());
        //
        if (userOptional != null) {

            if (user.getAlergies() != null) {
                user.setAlergies(AES.encrypt(user.getAlergies(), user.getUsername() + user.getPassword()));
            }

            if (user.getBloodType() != null) {
                user.setBloodType(AES.encrypt(user.getBloodType(), user.getUsername() + user.getPassword()));
            }
            if (user.getDisease() != null) {

                user.setDisease(AES.encrypt(user.getDisease(), user.getUsername() + user.getPassword()));
            }
            if(user.getMedicines()!=null)
            {
               user.setMedicines(AES.encrypt(user.getMedicines(), user.getUsername() + user.getPassword()));

            }

            userRepo.saveAndFlush(user);


            if (user.getLat() != null && user.getLon() != null) {

                try{
                String municipality = serviceCluster.returnClusterValueDimoi(new Point(user.getLat(), user.getLon()),
                        "en");
                byte[] ptext = municipality.getBytes(ISO_8859_1);
                String municipalityText = new String(ptext, StandardCharsets.UTF_8);
                System.out.println(municipalityText);

                user.setMunicipality(convert(municipalityText));

                String nomos = serviceCluster.returnClusterValueNomoi(new Point((user.getLat()), (user.getLon())),
                        "en");
                user.setDepartment(nomos);

                String periferia = serviceCluster.returnClusterValue(new Point((user.getLat()), (user.getLon())), "en");
                user.setPeriphery(periferia);
                }catch(Exception e)
                {
                    return new ResponseEntity<>(new ApiResponse(202 ,"cluster crashed"), HttpStatus.ACCEPTED);

                }

            }

            // user.setUser_id(userFound.getUser_id());
            userRepo.saveAndFlush(user);
            return new ResponseEntity<>(new ApiResponse(200, "update user ok"), HttpStatus.OK);

        }

        return new ResponseEntity<>(new ApiResponse(208, "User not found"), HttpStatus.ALREADY_REPORTED);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/users/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody User userRegister) {

        CustomPasswordEncoder customPasswordEncoder = new CustomPasswordEncoder();
//fixed this
        userRegister.setPassword(customPasswordEncoder.encode(userRegister.getPassword().toString()));

        List<User> users = userRepo.findAll();
        for (User user : users) {
            if (user.getUsername().equals(userRegister.getUsername())) {
                return new ResponseEntity<>(new ApiResponse(208, "User already exists"), HttpStatus.ALREADY_REPORTED);
            }
        }

        userRepo.save(userRegister);

        return new ResponseEntity<>(new ApiResponse(200, userRegister.getUsername()), HttpStatus.OK);

    }


   
    @RequestMapping(method = RequestMethod.POST, value = "/users/update/location")
    public ResponseEntity<ApiResponse> updateUserLocation(@RequestBody User userLocation) {

        Optional<User> usr = userRepo.findById(userLocation.getUserId());
        User user = usr.get();

        if (user.getUsername().equals(userLocation.getUsername())) {
            user.setLat(userLocation.getLat());
            user.setLon(userLocation.getLon());
        }

        return new ResponseEntity<>(new ApiResponse(200, "User update location ok"), HttpStatus.OK);

    }

    @GetMapping("/users/verify")
    public ResponseEntity<ApiResponse> verifyUser(@RequestParam String email, @RequestParam String token) {
        List<TempUser> tempUsers = tempUserRepo.findAll();

        List<User> users = userRepo.findAll();

        for (TempUser tempUser : tempUsers) {
            for (User user : users) {

                if (tempUser.getEmail().equalsIgnoreCase(user.getEmail())) {

                    if (user.getEmail().equalsIgnoreCase(email)) {

                   

                        userRepo.save(user);
                        // return new ApiResponse(200, "Successful Verification");
                        return new ResponseEntity<>(new ApiResponse(200, "Successful Verification"), HttpStatus.OK);
                    }

                }
            }
        }

        // return new ApiResponse(501, "Verification Failed");

        return new ResponseEntity<>(new ApiResponse(501, "Verification Failed"), HttpStatus.NOT_IMPLEMENTED);

    }

    // --------------------------------------------LOGIN-------------------------------------------------------------------------------------

    @RequestMapping(method = RequestMethod.POST, value = "/users/login")
    public ResponseEntity<ApiResponse> androidLogin(@RequestBody AuthenticationRequest auth) throws Exception {

        List<User> loginList = userRepo.findAll();
        List<Ambulance> ambulanceList = ambulanceRepo.findAll();

        for (User loginUser : loginList) {
            if (auth.getUsername().equalsIgnoreCase(loginUser.getUsername())
                    && auth.getPassword().equals(loginUser.getPassword())) {
                if (loginUser.getIsActive() == 1) {
                    try {
                        authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword()));
                    } catch (BadCredentialsException e) {
                        throw new Exception("Incorrect username or password", e);
                    }

                    final UserDetails userDetails = userDetailsService.loadUserByUsername(auth.getUsername());

                    final String jwt = jwtTokenUtil.generateToken(userDetails);
                    userRepo.saveAndFlush(loginUser);
                   

                    return new ResponseEntity<>(new ApiResponse(200, jwt, loginUser), HttpStatus.OK);
                } else {
                    userRepo.saveAndFlush(loginUser);
                    return new ResponseEntity<>(new ApiResponse(401, loginUser), HttpStatus.UNAUTHORIZED);

                }

            }
        }

        for (Ambulance ambulance : ambulanceList) {
            System.out.println(ambulance.toString());
            if (auth.getUsername().equalsIgnoreCase(ambulance.getUsername())
                    && auth.getPassword().equals(ambulance.getPassword())) {
                if (ambulance.getIsActive() == 1) {
                    ambulanceRepo.saveAndFlush(ambulance);

                    return new ResponseEntity<>(new ApiResponse(200, ambulance.getImei(), ambulance), HttpStatus.OK);
                } else {
                    ambulanceRepo.saveAndFlush(ambulance);
                    

                    return new ResponseEntity<>(new ApiResponse(401, ambulance), HttpStatus.UNAUTHORIZED);

                }
            }
        }

        // return new ApiResponse(404, "Login Failed. User Not Found");

        return new ResponseEntity<>(new ApiResponse(404, "Login Failed. User Not Found"), HttpStatus.NOT_FOUND);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/imei")
    public ResponseEntity<ApiResponse> androidLogin(@RequestParam String imei) {

        List<User> userList = userRepo.findAll();

        for (User user : userList) {
            if (user.getPhone().equals(imei)) {
                // return new ApiResponse(200, "User with this imei found", user);

                return new ResponseEntity<>(new ApiResponse(200, "User with this imei found", user), HttpStatus.OK);

            }
        }

        // return new ApiResponse(404, "Login Failed. User Not Found");

        return new ResponseEntity<>(new ApiResponse(404, "Login Failed. User Not Found"), HttpStatus.NOT_FOUND);

    }

    // ------------------------------- AED -----------------------------------

    @GetMapping("/aeds")
    public List<AED> aeds() {

        return aedRepo.findAll();

    }

    @RequestMapping(method = RequestMethod.GET, value = "/aeds/radius")
    public List<AED> getAEDSByRadius(@RequestParam Double lat, @RequestParam Double lon, @RequestParam Double radius) {

        List<AED> aeds = aedRepo.findAll();
        List<AED> aedsInRadius = new ArrayList<AED>();

        for (AED aed : aeds) {
            if (aed.getLat() != null && aed.getLon() != null) {

                Location center = new Location("User's location", lat, lon);
                Location test = new Location("Aed's location", Double.parseDouble(aed.getLat()),
                        Double.parseDouble(aed.getLon()));
                double distanceInMeters = Location.convertIntoKms(center.distanceTo(test));
                boolean isWithinRadius = distanceInMeters < radius;

                if (isWithinRadius) {
                    aedsInRadius.add(aed);
                }
                System.out.println("Distance " + distanceInMeters + "  In radius : " + isWithinRadius);
            }

        }
        // if (aedsInRadius.isEmpty()) {
        // aedsInRadius.add(aeds.get(0));
        // }

        return aedsInRadius;

    }

    @GetMapping("/aeds/{aedId}")
    public Optional<AED> getAED(@PathVariable int aedId) {

        return aedRepo.findById(aedId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/aeds/city")
    public List<AED> getAEDSByCity(@RequestParam String aedCity) {
        String cities[] = aedCity.split(",");

        List<AED> aeds = aedRepo.findAll();
        List<AED> aedsByCity = new ArrayList<AED>();

        for (AED aed : aeds) {
            for (String city : cities) {

                if (aed.getCity().equalsIgnoreCase(city)) {
                    aedsByCity.add(aed);
                }
            }

        }

        return aedsByCity;
    }

    @GetMapping("/aeds/users/{userId}")
    public List<AED> getUserAEDs(@PathVariable int userId) {

        List<AED> aeds = aedRepo.findAll();
        List<AED> userAeds = new ArrayList<AED>();

        for (AED aed : aeds) {

            if (aed.getUser().getUserId() == userId) {
                userAeds.add(aed);
            }
        }

        return userAeds;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/aeds/delete/{aedID}")
    public ResponseEntity<ApiResponse> deleteAED(@PathVariable int aedID) {
        aedRepo.deleteById(aedID);

        return new ResponseEntity<>(new ApiResponse(200, "Delete Aed Ok"), HttpStatus.OK);

        // return new ApiResponse(200, "Delete Aed Ok");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/aeds/add")
    public ResponseEntity<ApiResponse> addAED(@RequestBody AED aed) {

        try {
            System.out.println("\nAED ID: " + aed.getAed_id() + " Coordinates: " + aed.getLat() + " " + aed.getLon());
            if (aed.getLat() != null) {
                String municipality = serviceCluster.returnClusterValueDimoi(
                        new Point(Double.parseDouble(aed.getLat()), Double.parseDouble(aed.getLon())), "en");
                byte[] ptext = municipality.getBytes(ISO_8859_1);
                String municipalityText = new String(ptext, StandardCharsets.UTF_8);
                aed.setMunicipality(convert(municipalityText));
            }
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("Error on cluster could'nt parse aed");
            aed.setMunicipality("-");

        }

        String nomos = serviceCluster.returnClusterValueNomoi(
                new Point((Double.parseDouble(aed.getLat())), (Double.parseDouble(aed.getLon()))), "en");
        aed.setDepartment(nomos);

        String periferia = serviceCluster.returnClusterValue(
                new Point((Double.parseDouble(aed.getLat())), (Double.parseDouble(aed.getLon()))), "en");
        aed.setPeriphery(periferia);

        aedRepo.saveAndFlush(aed);
        return new ResponseEntity<>(new ApiResponse(200, String.valueOf(aed.getAed_id())), HttpStatus.OK);

    }

    @PostMapping("/aeds/image/upload")
    public ResponseEntity<ApiResponse> uploadAedImage(@RequestParam("file") MultipartFile file) {

        try {
            System.out.println(file.getOriginalFilename());

            System.out.println(file.getName());
            // String fileName=
            fileStorageServiceAed.storeFile(file);

            // String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            // .path("home/src/main/resources/static/images/aedImages/").path(fileName).toUriString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(400, e.toString()), HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<>(new ApiResponse(200, "Image upload ok"), HttpStatus.OK);
    }

    @PostMapping("/mobileusers/image/upload")
    public ResponseEntity<ApiResponse> uploadFile(@RequestParam("file") MultipartFile file) {

        try {
            fileStorageServiceMobile.storeFile(file);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(400, e.toString()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(200, "Image upload ok"), HttpStatus.OK);
    }

	
	@RequestMapping(method = RequestMethod.GET, value = "/marketing/channel/receive")
	public Flux<MarketingObject> getMarketingMessage() {
		return Flux.create(sink -> {
        	marketingProcessor.register(sink::next);
        });
    }
    
  
    // ------------------------------- EVENT -----------------------------------
    @GetMapping("/events")
    public List<Event> events() {

        return eventRepo.findAll();

    }

    @GetMapping("/events/{eventId}")
    public Optional<Event> getEvent(@PathVariable int eventId) {

        return eventRepo.findById(eventId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/events/delete/{eventID}")
    public ResponseEntity<ApiResponse> deleteEvent(@PathVariable int eventID) {
        eventRepo.deleteById(eventID);
        // return new ApiResponse(200, "Delete Event Ok");
        return new ResponseEntity<>(new ApiResponse(200, "Delete Event Ok"), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/events/add")
    public ResponseEntity<ApiResponse> addEvent(@RequestBody Event event) {

        System.out.println(
                "\n\n\n\n-------- Cluster OutPut ---------\nEvent From User: " + event.getUser().getEmail() + "\n"
                        + serviceCluster.returnClusterValue(
                                new Point(event.getLat(),event.getLon()), "en")
                        + "\n-----Cluster Ended--------");
        System.out.println("\n\n\n");

        System.out.println(
                "\n\n\n\n-------- ClusterNomos OutPut ---------\nEvent From User: " + event.getUser().getEmail() + "\n"
                        + serviceCluster.returnClusterValueNomoi(
                                new Point(event.getLat(), event.getLon()), "en")
                        + "\n-----Cluster Ended--------");
        System.out.println("\n\n\n");

        System.out.println(
                "\n\n\n\n-------- ClusterDimos OutPut ---------\nEvent From User: " + event.getUser().getEmail() + "\n"
                        + serviceCluster.returnClusterValueDimoi(
                                new Point(event.getLat(), event.getLon()), "en")
                        + "\n-----Cluster Ended--------");
        System.out.println("\n\n\n");

        event.setPeriphery(serviceCluster.returnClusterValue(
                new Point(event.getLat(), event.getLon()), "en"));
        event.setDepartment(serviceCluster.returnClusterValueNomoi(
                new Point(event.getLat(), event.getLon()), "en"));

        String municipality = serviceCluster.returnClusterValueDimoi(
                new Point(event.getLat(), event.getLon()), "en");
        byte[] ptext = municipality.getBytes(ISO_8859_1);
        String municipalityText = new String(ptext, UTF_8);

        event.setMunicipality(convert(municipalityText));

        eventRepo.saveAndFlush(event);

        User usr = event.getUser();
        String decryptSecret = usr.getUsername() + usr.getPassword();

        if (usr.getAlergies() != null && !usr.getAlergies().equalsIgnoreCase("null")) {
            usr.setAlergies(AES.decrypt(usr.getAlergies(), decryptSecret));
        }
        if (usr.getDisease() != null && !usr.getDisease().equalsIgnoreCase("null")) {
            usr.setDisease(AES.decrypt(usr.getDisease(), decryptSecret));
        }

        if (usr.getBloodType() != null && !usr.getBloodType().equalsIgnoreCase("null")) {
            usr.setBloodType(AES.decrypt(usr.getBloodType(), decryptSecret));
        }

        if (usr.getMedicines() != null && !usr.getMedicines().equalsIgnoreCase("null")) {
            usr.setMedicines(AES.decrypt(usr.getMedicines(), decryptSecret));
        }
        event.setUser(usr);

        if (event.getHospital() != null
                && event.getHospital().getEventProcessor().equalsIgnoreCase("Agios-Loukas-Clinic")) {
            agios_loukas_processor.process(event);
        } else if (event.getHospital() != null
                && event.getHospital().getEventProcessor().equalsIgnoreCase("Iaso-Thessalias-Clinic")) {
            iaso_thessalias_processor.process(event);
        }

       
        return new ResponseEntity<>(new ApiResponse(200, "Added Ok"), HttpStatus.OK);

    }

    @GetMapping(path = "/events/receive/{clinic}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Event> receive(@PathVariable String clinic) {
        // Some FluxSink documentation and code samples:
        // - h
        // - https://www.baeldung.com/reactor-core
        // -
        // https://www.e4developer.com/2018/04/14/webflux-and-servicing-client-requests-how-does-it-work/

        if (clinic.equalsIgnoreCase("Agios_Loukas_Processor")) {
            return Flux.create(sink -> {
                agios_loukas_processor.register(sink::next);
            });
        } else if (clinic.equalsIgnoreCase("Iaso_Thessalias_Processor")) {
            return Flux.create(sink -> {
                iaso_thessalias_processor.register(sink::next);
            });
        }
      
        return null;

    }

    @RequestMapping(method = RequestMethod.GET, value = "/events/city")
    public List<Event> eventsbycity(@RequestParam String eventCity) {

        List<Event> events = eventRepo.findAll();
        List<Event> eventbycity = new ArrayList<Event>();

        String cities[] = eventCity.split(",");

        for (Event event : events) {
            for (String city : cities) {
                if (event.getCity().equalsIgnoreCase(city)) {
                    eventbycity.add(event);
                }
            }

        }
        return eventbycity;

    }
    // -------------------------------REPORTS -----------------------------------

    @GetMapping("/reports")
    public List<Reports> reports() {

        return reportsRepo.findAll();

    }

    @GetMapping("/reports/{reportId}")
    public Optional<Reports> getReport(@PathVariable int reportId) {

        return reportsRepo.findById(reportId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/reports/delete/{reportId}")
    public ResponseEntity<ApiResponse> deleteReport(@PathVariable int reportId) {
        reportsRepo.deleteById(reportId);
        // return new ApiResponse(200, "Delete Report Ok");

        return new ResponseEntity<>(new ApiResponse(200, "Delete Report Ok"), HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/reports/add")
    public ResponseEntity<ApiResponse> addReport(@RequestBody Reports report) {
        reportsRepo.saveAndFlush(report);
        // return new ApiResponse(200, "Insert Reports Ok");
        return new ResponseEntity<>(new ApiResponse(200, "Insert Reports Ok"), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/asirmatistis/add")
    public ResponseEntity<ApiResponse> addEventTo(@RequestBody Event event) {
        eventRepo.saveAndFlush(event);
        asirmatistisProcessor.process(event);

        return new ResponseEntity<>(new ApiResponse(200, "Insert Event Ok"), HttpStatus.OK);
    }

    @GetMapping(path = "/asirmatistis/receive", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Event> receiveEvents() {
        // Some FluxSink documentation and code samples:

        // - h
        // - https://www.baeldung.com/reactor-core
        // -
        // https://www.e4developer.com/2018/04/14/webflux-and-servicing-client-requests-how-does-it-work/

        return Flux.create(sink -> {
            asirmatistisProcessor.register(sink::next);
        });
    }

    @RequestMapping(method = RequestMethod.POST, value = "/completed/event")
    public ResponseEntity<ApiResponse> addEventToCompleted(@RequestBody Event event) {

        completedProcessor.process(event);

        return new ResponseEntity<>(new ApiResponse(200, "Complete To Radio Ok"), HttpStatus.OK);
    }

    @GetMapping(path = "/completed/receive", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Event> receiveCompleted() {

        return Flux.create(sink -> {
            completedProcessor.register(sink::next);
        });
    }

    // ----------------------------------- AMBULANCE
    // -----------------------------------

    @GetMapping("/ambulance")
    public List<Ambulance> ambulances() {
        return ambulanceRepo.findAll();
    }

    @GetMapping("/ambulance/{ambulanceId}")
    public Optional<Ambulance> getAmbulance(@PathVariable int ambulanceId) {

        return ambulanceRepo.findById(ambulanceId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/ambulance/delete/{ambulanceId}")
    public ResponseEntity<ApiResponse> deleteAmbulance(@PathVariable int ambulanceId) {
        ambulanceRepo.deleteById(ambulanceId);

        return new ResponseEntity<>(new ApiResponse(200, "Delete Ambulance Ok"), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/ambulance/add")
    public ResponseEntity<ApiResponse> addReport(@RequestBody Ambulance ambulance) {
        ambulanceRepo.saveAndFlush(ambulance);
        ambulanceProcessor.process(ambulance);
        return new ResponseEntity<>(new ApiResponse(200, "Insert Ambulance Ok"), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/ambulance/assign/add")
    public ResponseEntity<ApiResponse> addAssign(@RequestBody Event event) {
        eventRepo.saveAndFlush(event);
        assignProcessor.process(event);
        return new ResponseEntity<>(new ApiResponse(200, "ok assign"), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/ambulance/assign/receive")
    public Flux<Event> addReport() {

        return Flux.create(sink -> {
            assignProcessor.register(sink::next);
        });
    }

    @GetMapping(path = "/ambulance/receive", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Ambulance> receiveAmbulance(HttpServletResponse r) {

        r.addHeader("Connection", "Keep-Alive");
        r.addHeader("Keep-Alive", "timeout=5, max=1000");
        // Some FluxSink documentation and code samples:
        // - h
        // - https://www.baeldung.com/reactor-core
        // -
        // https://www.e4developer.com/2018/04/14/webflux-and-servicing-client-requests-how-does-it-work/

        return Flux.create(sink -> {
            ambulanceProcessor.register(sink::next);
        });
    }


    @RequestMapping(method = RequestMethod.POST, value = "/android/ambulance/authenticate")
    public ResponseEntity<ApiResponse> authAmbulance(@RequestBody AuthenticationRequest auth) {
        
        List<Ambulance> ambulances = ambulanceRepo.findAll();
        
        for(Ambulance ambulance:ambulances)
        {
            if(ambulance.getUsername().equals(auth.getUsername()) && ambulance.getPassword().equals(auth.getPassword()))
            {
                return new ResponseEntity<>(new ApiResponse(200, ambulance), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ApiResponse(404, "Ambulance authentication failed"), HttpStatus.NOT_FOUND);
    }

    // ------------------------------- SMS -----------------------------------

    @GetMapping("/sms")
    public List<Sms> sms() {

        return smsRepository.findAll();

    }

    @GetMapping("/sms/{smsId}")
    public Optional<Sms> getSms(@PathVariable int smsId) {

        return smsRepository.findById(smsId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/sms/delete/{smsID}")
    public ResponseEntity<ApiResponse> deleteSms(@PathVariable int smsID) {
        smsRepository.deleteById(smsID);
        return new ResponseEntity<>(new ApiResponse(200, "Delete Sms Ok"), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/sms/add")
    public ResponseEntity<ApiResponse> addSms(@RequestBody Sms sms) {
        smsRepository.saveAndFlush(sms);
        return new ResponseEntity<>(new ApiResponse(200, "Insert Sms Ok"), HttpStatus.OK);
    }

    @GetMapping("/sms/users/{userId}")
    public List<Sms> getUserSms(@PathVariable int userId) {

        List<Sms> smses = smsRepository.findAll();
        List<Sms> userSms = new ArrayList<Sms>();

        for (Sms sms : smses) {
            if (sms.getUser().getUserId() == userId) {
                userSms.add(sms);
            }
        }

        return userSms;
    }

    // ------------------------------------LOGS-----------------------------------------------
    // @GetMapping("/logs")
    // public String printLogs() {
    // StringBuilder strLine = null;
    //
    // try {
    // FileInputStream fstream = new FileInputStream("logs/spring.log");
    // BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
    // strLine = new StringBuilder();
    // /* read log line by line */
    // while (br.readLine() != null) {
    // /* parse strLine to obtain what you want */
    // strLine.append(br.readLine());
    // System.out.println(strLine);
    // }
    // fstream.close();
    // } catch (Exception e) {
    // System.err.println("Error: " + e.getMessage());
    // }
    // return strLine.toString();
    //
    // }

    // ---------------------------------- Cluter
    // ---------------------------------------

    @GetMapping("/aeds/cluster")
    public void doClusterAed() {
        List<AED> allAeds = aedRepo.findAll();
        for (AED aed : allAeds) {
            try {
                System.out
                        .println("\nAED ID: " + aed.getAed_id() + " Coordinates: " + aed.getLat() + " " + aed.getLon());
                if (aed.getLat() != null) {
                    String municipality = serviceCluster.returnClusterValueDimoi(
                            new Point(Double.parseDouble(aed.getLat()), Double.parseDouble(aed.getLon())), "en");
                    byte[] ptext = municipality.getBytes(ISO_8859_1);
                    String municipalityText = new String(ptext, StandardCharsets.UTF_8);
                    System.out.println(convert(municipalityText));
                    aed.setMunicipality(convert(municipalityText));
                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Error on cluster could'nt parse aed");
                aed.setMunicipality("-");

            }

            String nomos = serviceCluster.returnClusterValueNomoi(
                    new Point((Double.parseDouble(aed.getLat())), (Double.parseDouble(aed.getLon()))), "en");
            aed.setDepartment(nomos);

            String periferia = serviceCluster.returnClusterValue(
                    new Point((Double.parseDouble(aed.getLat())), (Double.parseDouble(aed.getLon()))), "en");
            aed.setPeriphery(periferia);

        }
        aedRepo.saveAll(allAeds);

    }

    // -------------------------------------------------NOTIFICATION NEAREST
    // ---------------------------------------------------

    @RequestMapping(value = "/android/receive/nearest/volunteer", method = RequestMethod.GET)
    public Flux<Event> getEventVolunteers() {
        return Flux.create(sink -> {
            notificationProcessor.register(sink::next);
        });

    }

    @RequestMapping(value = "/android/notify/nearest/volunteer", method = RequestMethod.POST)
    public void registerToEvent(@RequestBody Event event) {
        notificationProcessor.process(event);
    }

    @GetMapping("/users/cluster")
    public void doClusterUser() {
        List<User> allUsers = userRepo.findAll();
        for (User user : allUsers) {
            try {
                System.out.println(
                        "\nAED ID: " + user.getUserId() + " Coordinates: " + user.getLat() + " " + user.getLon());
                if (user.getLat() != null) {

                    String municipality = serviceCluster
                            .returnClusterValueDimoi(new Point(user.getLat(), user.getLon()), "en");
                    byte[] ptext = municipality.getBytes(ISO_8859_1);
                    String municipalityText = new String(ptext, StandardCharsets.UTF_8);
                    System.out.println(convert(municipalityText));
                    user.setMunicipality(convert(municipalityText));

                    String nomos = serviceCluster.returnClusterValueNomoi(new Point((user.getLat()), (user.getLon())),
                            "en");
                    user.setDepartment(nomos);

                    String periferia = serviceCluster.returnClusterValue(new Point((user.getLat()), (user.getLon())),
                            "en");
                    user.setPeriphery(periferia);

                    // System.out.println("\n\n\n\n-------- Cluster OutPut ---------\nEvent From
                    // User: "+event.getUser().getEmail()+"\n"+serviceCluster.returnClusterValue(new
                    // Point(Double.parseDouble(event.getLat()),Double.parseDouble(event.getLon())),
                    // "en")+"\n-----Cluster Ended--------");
                    // System.out.println("\n\n\n");
                    //
                    // System.out.println("\n\n\n\n-------- ClusterNomos OutPut ---------\nEvent
                    // From User:
                    // "+event.getUser().getEmail()+"\n"+serviceCluster.returnClusterValueNomoi(new
                    // Point(Double.parseDouble(event.getLat()),Double.parseDouble(event.getLon())),
                    // "en")+"\n-----Cluster Ended--------");
                    // System.out.println("\n\n\n");
                    //
                    // System.out.println("\n\n\n\n-------- ClusterDimos OutPut ---------\nEvent
                    // From User:
                    // "+event.getUser().getEmail()+"\n"+serviceCluster.returnClusterValueDimoi(new
                    // Point(Double.parseDouble(event.getLat()),Double.parseDouble(event.getLon())),
                    // "en")+"\n-----Cluster Ended--------");
                    // System.out.println("\n\n\n");
                    //
                    // event.setPeriphery(serviceCluster.returnClusterValue(new
                    // Point(Double.parseDouble(event.getLat()),Double.parseDouble(event.getLon())),"en"));
                    // event.setDepartment(serviceCluster.returnClusterValueNomoi(new
                    // Point(Double.parseDouble(event.getLat()),Double.parseDouble(event.getLon())),"en"));
                    //
                    //
                    // String municipality=serviceCluster.returnClusterValueDimoi(new
                    // Point(Double.parseDouble(event.getLat()),Double.parseDouble(event.getLon())),"en");
                    // byte[] ptext = municipality.getBytes(ISO_8859_1);
                    // String municipalityText = new String(ptext, UTF_8);
                    //
                    // event.setMunicipality(municipalityText);

                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error on cluster could'nt parse user");
                user.setMunicipality("-");

            }
        }
        userRepo.saveAll(allUsers);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/hospitals/radius")
    public List<Hospital> getHospitalsByRadius(@RequestParam Double lat, @RequestParam Double lon,
            @RequestParam Double radius) {

        List<Hospital> hospitals = hospitalRepo.findAll();
        List<Hospital> hospitalsInRadius = new ArrayList<Hospital>();

        for (Hospital hospital : hospitals) {
            if (hospital.getLat() != 0 && hospital.getLon() != 0) {

                Location center = new Location("User's location", lat, lon);
                Location test = new Location("Hospitals location", hospital.getLat(), hospital.getLon());

                double distanceInMeters = Location.convertIntoKms(center.distanceTo(test));
                boolean isWithinRadius = distanceInMeters < radius;

                if (isWithinRadius) {
                    hospitalsInRadius.add(hospital);
                }
                System.out.println("Distance " + distanceInMeters + "  In radius : " + isWithinRadius);
            }

        }

        return hospitalsInRadius;

    }

    @RequestMapping(method = RequestMethod.POST, value = "/nearest/event/hospital")
    public ResponseEntity<List<Hospital>> getClinics(@RequestBody Event event) {

        List<Hospital> hospitals = hospitalRepo.findAll();
        List<Hospital> inClusterHospitals = new ArrayList<>();
        // HashMap<Double,Hospital> nearestHospitals = new HashMap<>();
        // List<Double> minDist = new ArrayList<>();

        String municipality = serviceCluster.returnClusterValueDimoi(
                new Point(event.getLat(),event.getLon()), "en");
        byte[] ptext = municipality.getBytes(ISO_8859_1);
        String municipalityText = new String(ptext, StandardCharsets.UTF_8);
        System.out.println(municipalityText);

        event.setMunicipality(convert(municipalityText));

        for (Hospital hospital : hospitals) {

            if (event.getUser().getHospital() != null) // an exei sumvolaio
            {

                if (event.getUser().getHospital().getName().equalsIgnoreCase(hospital.getName())
                        && hospital.getClusterMunicipality().contains(event.getMunicipality())) // an o xristis exei
                                                                                                // sumvolaio me kapoia
                                                                                                // kliniki
                {
                    inClusterHospitals.add(hospital);
                    return new ResponseEntity<>(inClusterHospitals, HttpStatus.OK);
                } else {
                    // partnerships
                }

            }

            else if (hospital.getClusterMunicipality().contains(event.getMunicipality())) {
                inClusterHospitals.add(hospital);
            }
        }

        return new ResponseEntity<>(inClusterHospitals, HttpStatus.CREATED);

    }

    @RequestMapping(value = "/android/update/userlocation", method = RequestMethod.POST)
    public HttpStatus updateUserLocation(@RequestBody UserLocation location) {
        Optional<User> userFromRepo = userRepo.findByUsername(location.getUsername());

        // Posted Data
        Double lat = location.getLat();
        Double lonString = location.getLon();

        // User from user_id
        User user = userFromRepo.get();

        user.setLat(lat);
        user.setLon(lonString);

        userRepo.saveAndFlush(user);
        userProcessor.process(location);

        return HttpStatus.OK;

    }

    @RequestMapping(value = "/android/receive/userlocation", method = RequestMethod.GET)
    public Flux<UserLocation> getUserLocation() {
        return Flux.create(sink -> {
            userProcessor.register(sink::next);
        });

    }

    public static String convert(String name) {

        String greeklish = name.toLowerCase();
        StringBuilder sb = new StringBuilder();
        Map<String, String> values = new HashMap<String, String>();

        values.put("α", "a");
        values.put("ά", "a");
        values.put("β", "b");
        values.put("γ", "g");
        values.put("δ", "d");
        values.put("ε", "e");
        values.put("έ", "e");
        values.put("ζ", "z");
        values.put("η", "i");
        values.put("ή", "i");
        values.put("θ", "th");
        values.put("ι", "i");
        values.put("ί", "i");
        values.put("ϊ", "i");
        values.put("κ", "k");
        values.put("λ", "l");
        values.put("μ", "m");
        values.put("ν", "n");
        values.put("ξ", "x");
        values.put("ο", "o");
        values.put("ό", "o");
        values.put("π", "p");
        values.put("ρ", "r");
        values.put("σ", "s");
        values.put("ς", "s");
        values.put("τ", "t");
        values.put("υ", "y");
        values.put("ύ", "y");
        values.put("φ", "f");
        values.put("χ", "x");
        values.put("ψ", "ps");
        values.put("ω", "w");
        values.put("ώ", "w");
        values.put(" ", "-");
        values.put("!", "");
        values.put("", "");
        values.put("@", "");
        values.put("#", "");
        values.put("$", "");
        values.put("%", "");
        values.put("&", "");
        values.put("=", "");
        values.put("+", "-");
        values.put("*", "");
        values.put("{", "");
        values.put("}", "");
        values.put("?", "");
        values.put("[", "");
        values.put("]", "");
        values.put("(", "");
        values.put(")", "");
        values.put("\\", "");
        values.put("_", "");
        values.put(",", "");
        values.put("|", "");
        values.put("΅", "");
        values.put("'", "");
        values.put(";", "");
        values.put(":", "");
        values.put("/", "");
        values.put(">", "");
        values.put("<", "");
        values.put("`", "");
        values.put("^", "");
        values.put(".", "");
        values.put("``", "");
        values.put("~", "");
        values.put("«", "");
        values.put("»", "");
        values.put("�", "y");

        for (int i = 0; i < greeklish.length(); i++) {

            if (i == 0) {

                if (greeklish.charAt(0) == 'ψ' || greeklish.charAt(0) == 'Ψ' || greeklish.charAt(0) == 'θ'
                        || greeklish.charAt(0) == 'Θ') {

                    if (greeklish.charAt(0) == 'ψ' || greeklish.charAt(0) == 'Ψ') {
                        sb.append("Ps");
                    }

                    else if (greeklish.charAt(0) == 'θ' || greeklish.charAt(0) == 'Θ') {
                        sb.append("Th");
                    }

                }

                else {

                    if (values.containsKey(String.valueOf(greeklish.charAt(i)))) {
                        sb.append((values.get(String.valueOf(greeklish.charAt(i))).toUpperCase()));

                    } else {
                        sb.append((String.valueOf((greeklish.charAt(i)))).toUpperCase());
                    }
                }
            } else {
                if (values.containsKey(String.valueOf(greeklish.charAt(i)))) {
                    sb.append((values.get(String.valueOf(greeklish.charAt(i)))));

                } else {
                    System.out.println("Den vrika to : " + String.valueOf(greeklish.charAt(i)));
                    sb.append((String.valueOf((greeklish.charAt(i)))));
                }
            }

        }

        return sb.toString();
    }

}