package com.aed.demo;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static java.nio.charset.StandardCharsets.*;

import java.nio.charset.StandardCharsets;

import com.aed.demo.cluster.InitServiceCluster;
import com.aed.demo.cluster.Point;
import com.aed.demo.entity.Ambulance;
import com.aed.demo.entity.ApiResponse;
import com.aed.demo.entity.Event;
import com.aed.demo.entity.MarketingObject;
import com.aed.demo.entity.User;
import com.aed.demo.fileupload.FileStorageService;
import com.aed.demo.fileupload.UploadFileResponse;
import com.aed.demo.processor.AmbulanceProcessor;
import com.aed.demo.processor.AsirmatistisProcessor;
import com.aed.demo.processor.AssignProcessor;
import com.aed.demo.processor.CompletedProcessor;
import com.aed.demo.processor.Agios_Loukas_Processor;
import com.aed.demo.processor.Iaso_Thessalias_Processor;
import com.aed.demo.processor.MarketingProcessor;
import com.aed.demo.processor.NotificationProcessor;
import com.aed.demo.repositories.AmbulanceRepository;
import com.aed.demo.repositories.EventRepository;
import com.aed.demo.repositories.MarketingRepository;
import com.aed.demo.repositories.UserRepository;
import com.aed.demo.security.AES;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/web")
public class RestControllerWeb {

    @Autowired
    UserRepository userRepo;

    @Autowired
    AmbulanceRepository ambulanceRepo;

    @Autowired
    EventRepository eventRepo;

    @Autowired
    MarketingRepository marketingRepo;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private AmbulanceProcessor ambulanceProcessor;

    @Autowired
    private AssignProcessor assignProcessor;

    @Autowired
    private AsirmatistisProcessor asirmatistisProcessor;

    @Autowired
    private CompletedProcessor completedProcessor;

    @Autowired
    private MarketingProcessor marketingProcessor;

    @Autowired
    private Agios_Loukas_Processor agios_loukas_processor;

    @Autowired
    private Iaso_Thessalias_Processor iaso_thessalias_processor;

    @Autowired
    private NotificationProcessor notificationProcessor;

    @Autowired
    private InitServiceCluster serviceCluster;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {

        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("home/src/main/resources/static/images/profileImages/").path(fileName).toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/users/add")
    public ResponseEntity<ApiResponse> addUser(@RequestBody User user) throws Exception {
        userRepo.save(user); // an o xristis uparxei kai exei allaksei to firebase token ananewse ton
        return new ResponseEntity<>(new ApiResponse(200, user), HttpStatus.OK);
    }

    @GetMapping(path = "/events/receive/{clinic}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Event> receive(@PathVariable String clinic) {
        // Some FluxSink documentation and code samples:
        // - h
        // - https://www.baeldung.com/reactor-core
        // -
        // https://www.e4developer.com/2018/04/14/webflux-and-servicing-client-requests-how-does-it-work/

        if (clinic.equalsIgnoreCase("Agios-Loukas-Clinic")) {
            return Flux.create(sink -> {
                agios_loukas_processor.register(sink::next);
            });
        } else if (clinic.equalsIgnoreCase("Iaso-Thessalias-Clinic")) {
            return Flux.create(sink -> {
                iaso_thessalias_processor.register(sink::next);
            });
        }

        // else if(peryphery.equalsIgnoreCase("P-Dytikis-Makedonias"))
        // {
        // return Flux.create(sink -> {
        // dytikis_Makedonias.register(sink::next);
        // });
        // }else if(peryphery.equalsIgnoreCase("P-Hpeirou"))
        // {
        // return Flux.create(sink -> {
        // hpeirou.register(sink::next);
        // });
        // }else if(peryphery.equalsIgnoreCase("P-Thessalias"))
        // {
        // return Flux.create(sink -> {
        // thessalias.register(sink::next);
        // });
        // }else if(peryphery.equalsIgnoreCase("P-Boreiou-Aigaiou"))
        // {
        // return Flux.create(sink -> {
        // boreiou_Aigaiou.register(sink::next);
        // });
        // }else if(peryphery.equalsIgnoreCase("P-Notiou-Aigaiou"))
        // {
        // return Flux.create(sink -> {
        // notiou_Aigaiou.register(sink::next);
        // });
        // }else if(peryphery.equalsIgnoreCase("P-Stereas-Elladas"))
        // {
        // return Flux.create(sink -> {
        // stereas_Elladas.register(sink::next);
        // });
        // }else if(peryphery.equalsIgnoreCase("P-Peloponnisou"))
        // {
        // return Flux.create(sink -> {
        // peloponnisou.register(sink::next);
        // });
        // }else if(peryphery.equalsIgnoreCase("P-Ionion-Nison"))
        // {
        // return Flux.create(sink -> {
        // ionion_Nison.register(sink::next);
        // });
        // }else if(peryphery.equalsIgnoreCase("P-Kritis"))
        // {
        // return Flux.create(sink -> {
        // kritis.register(sink::next);
        // });
        // }else if(peryphery.equalsIgnoreCase("P-Attikis"))
        // {
        // return Flux.create(sink -> {
        // attikis.register(sink::next);
        // });
        // }else if(peryphery.equalsIgnoreCase("P-Dytikis-Elladas"))
        // {
        // return Flux.create(sink -> {
        // dytikis_Elladas.register(sink::next);
        // });
        // }
        return null;

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

    @RequestMapping(method = RequestMethod.POST, value = "/ambulance/add")
    public ResponseEntity<ApiResponse> addReport(@RequestBody Ambulance ambulance) {
        ambulanceRepo.saveAndFlush(ambulance);
        ambulanceProcessor.process(ambulance);
        return new ResponseEntity<>(new ApiResponse(200, "Insert Ambulance Ok"), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/asirmatistis/add")
    public ResponseEntity<ApiResponse> addEventTo(@RequestBody Event event) {
        eventRepo.saveAndFlush(event);
        asirmatistisProcessor.process(event);

        return new ResponseEntity<>(new ApiResponse(200, "Insert Event Ok"), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/completed/event")
    public ResponseEntity<ApiResponse> addEventToCompleted(@RequestBody Event event) {

        completedProcessor.process(event);

        return new ResponseEntity<>(new ApiResponse(200, "Complete To Radio Ok"), HttpStatus.OK);
    }

    @GetMapping(path = "/completed/receive", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Event> receiveCompleted() {

        // Some FluxSink documentation and code samples:
        // - h
        // - https://www.baeldung.com/reactor-core
        // -
        // https://www.e4developer.com/2018/04/14/webflux-and-servicing-client-requests-how-does-it-work/

        return Flux.create(sink -> {
            completedProcessor.register(sink::next);
        });
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

    @RequestMapping(method = RequestMethod.POST, value = "/ambulance/assign/add")
    public ResponseEntity<ApiResponse> addAssign(@RequestBody Event event) {

        User usr = event.getUser();

        String encryptSecret = usr.getUsername() + usr.getPassword();

        if (usr.getAlergies() != null && !usr.getAlergies().equalsIgnoreCase("null")) {
            usr.setAlergies(AES.encrypt(usr.getAlergies(), encryptSecret));
        }
        if (usr.getDisease() != null && !usr.getDisease().equalsIgnoreCase("null")) {
            usr.setDisease(AES.encrypt(usr.getDisease(), encryptSecret));
        }

        if (usr.getBloodType() != null && !usr.getBloodType().equalsIgnoreCase("null")) {
            usr.setBloodType(AES.encrypt(usr.getBloodType(), encryptSecret));
        }
        if (usr.getMedicines() != null && !usr.getMedicines().equalsIgnoreCase("null")) {
            usr.setMedicines(AES.encrypt(usr.getMedicines(), encryptSecret));
        }
        eventRepo.saveAndFlush(event);
        assignProcessor.process(event);
        return new ResponseEntity<>(new ApiResponse(200, "ok assign"), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/marketing/channel")
    public ResponseEntity<ApiResponse> addMarketingMessage(@RequestBody MarketingObject marketingObject) {
        marketingProcessor.process(marketingObject);
        marketingRepo.save(marketingObject);
        return new ResponseEntity<>(new ApiResponse(200, "Message Sent"), HttpStatus.OK);
    }

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

    @RequestMapping(method = RequestMethod.POST, value = "/events/add")
    public ResponseEntity<ApiResponse> addEvent(@RequestBody Event event) {

        System.out.println(
                "\n\n\n\n-------- Cluster OutPut ---------\nEvent From User: " + event.getUser().getEmail() + "\n"
                        + serviceCluster.returnClusterValue(
                                new Point(event.getLat(), event.getLon()), "en")
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
                new Point(event.getLat(),event.getLon()), "en"));
        event.setDepartment(serviceCluster.returnClusterValueNomoi(
                new Point(event.getLat(), event.getLon()), "en"));

        String municipality = serviceCluster.returnClusterValueDimoi(
                new Point(event.getLat(), event.getLon()), "en");
        byte[] ptext = municipality.getBytes(ISO_8859_1);
        String municipalityText = new String(ptext, UTF_8);

        event.setMunicipality(HomeResource.convert(municipalityText));

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

        // if (event.getPeriphery().equalsIgnoreCase("P-Anatolikis-Makedonias-Thrakis"))
        // {
        // anatolikis_Makedonias_Thrakis.process(event);
        //
        // } else if (event.getPeriphery().equalsIgnoreCase("P-Kentrikis-Makedonias")) {
        //
        // kentrikis_Makedonias.process(event);
        //
        // } else if (event.getPeriphery().equalsIgnoreCase("P-Dytikis-Makedonias")) {
        // dytikis_Makedonias.process(event);
        //
        // } else if (event.getPeriphery().equalsIgnoreCase("P-Hpeirou")) {
        // hpeirou.process(event);
        // } else if (event.getPeriphery().equalsIgnoreCase("P-Thessalias")) {
        // thessalias.process(event);
        // } else if (event.getPeriphery().equalsIgnoreCase("P-Boreiou-Aigaiou")) {
        // boreiou_Aigaiou.process(event);
        //
        // } else if (event.getPeriphery().equalsIgnoreCase("P-Notiou-Aigaiou")) {
        // notiou_Aigaiou.process(event);
        //
        // } else if (event.getPeriphery().equalsIgnoreCase("P-Stereas-Elladas")) {
        // stereas_Elladas.process(event);
        // } else if (event.getPeriphery().equalsIgnoreCase("P-Peloponnisou")) {
        // peloponnisou.process(event);
        // } else if (event.getPeriphery().equalsIgnoreCase("P-Ionion-Nison")) {
        // ionion_Nison.process(event);
        // } else if (event.getPeriphery().equalsIgnoreCase("P-Kritis")) {
        // kritis.process(event);
        // } else if (event.getPeriphery().equalsIgnoreCase("P-Attikis")) {
        // attikis.process(event);
        // } else if (event.getPeriphery().equalsIgnoreCase("P-Dytikis-Elladas")) {
        // dytikis_Elladas.process(event);
        // }

        // eventsProcessor.process(event);

        return new ResponseEntity<>(new ApiResponse(200, "Added Ok"), HttpStatus.OK);

    }

}
