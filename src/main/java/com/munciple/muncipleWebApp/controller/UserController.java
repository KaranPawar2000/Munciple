    package com.munciple.muncipleWebApp.controller;


    import com.munciple.muncipleWebApp.dto.Response;
    import com.munciple.muncipleWebApp.entity.User;
    import com.munciple.muncipleWebApp.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/users")
    public class UserController {

        @Autowired
        private UserService userService;


        @GetMapping("/whatsapp/{whatsappId}")
        public ResponseEntity<Response> getUserByWhatsappId(@PathVariable String whatsappId) {
            Response response = userService.getUserByWhatsappId(whatsappId);
            HttpStatus status = response.getStatus().equals("success") ? HttpStatus.OK : HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(response, status);
        }

        @PostMapping("/register")
        public ResponseEntity<Response> registerUser(@RequestBody User user) {
            // Validate required fields
            if (user.getName() == null ||
                    user.getWardNumber() == null || user.getWhatsappId() == null) {
                Response response = new Response();
                response.setStatus("error");
                response.setMessage("Required fields missing: name, phoneNumber, wardNumber and whatsappId are mandatory");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Response response = userService.registerUser(user);
            HttpStatus status = response.getStatus().equals("success") ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(response, status);
        }
    }
