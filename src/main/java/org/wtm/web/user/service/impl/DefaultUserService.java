//package org.wtm.web.user.service.impl;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Service;
//import org.wtm.web.user.service.UserService;
//
//@Service
//@RequiredArgsConstructor
//@Log4j2
//public class DefaultUserService implements UserService {
//
////    private final APIUserDetailsService apiUserDetailsService;
////    private final UploadService uploadService;
////
////    private final UserRepository userRepository;
////
////    private final PasswordEncoder passwordEncoder;
////    private final ModelMapper modelMapper;
////
////    @Value("${image.upload-profile-dir}")
////    private String uploadDir;
////
////
////    @Override
////    public void signUp(UserSignUpDto userSignUpDto) throws DuplicateEmailException {
////
////        String email = userSignUpDto.getEmail();
////
////        boolean exist = userRepository.existsByEmail(email);
////
////        if(exist) { throw new DuplicateEmailException(); }
////
////        User user = modelMapper.map(userSignUpDto, User.class);
////        user.updatePassword(passwordEncoder.encode(user.getPassword()));
////
////        log.info("================================");
////        log.info("User : {}", user);
////        log.info("User role : {}", user.getRole());
////
////        userRepository.save(user);
////    }
//}
