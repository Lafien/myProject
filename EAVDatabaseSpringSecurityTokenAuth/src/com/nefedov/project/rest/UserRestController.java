package com.nefedov.project.rest;

import com.nefedov.project.model.Message;
import com.nefedov.project.model.UserInfo;
import com.nefedov.project.service.MessageService;
import com.nefedov.project.service.UserService;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping(value = "/users/")
public class UserRestController {

    private final UserService userService;
    private final MessageService messageService;

    @Autowired
    public UserRestController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }


    @GetMapping(value = "contacts")
    public ResponseEntity<List<UserInfo>> getFriendsContact(Authentication authentication) {
        List<UserInfo> user = userService.findFriendContact(authentication.getName());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "myprofile")
    public ResponseEntity<UserInfo> getMyProfile(Authentication authentication) {
        UserInfo user = userService.findByUsernameInfo(authentication.getName());

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "contacts/{username}")
    public ResponseEntity<UserInfo> getInfoAboutContact(@PathVariable(name = "username") String username) {
        UserInfo user = userService.findByUsernameInfo(username);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PostMapping("contacts")
    public ResponseEntity<String> addContact(Authentication authentication, @RequestBody UserInfo userInfo) {

        if(userInfo.getUsername().isEmpty()){
            System.out.println(userInfo.getUsername());
            return  new ResponseEntity<>("Request in empty", HttpStatus.BAD_REQUEST);
        }

        if(userInfo.getUsername().equals(authentication.getName())){
            return new ResponseEntity<>("You cannot add yourself to friends", HttpStatus.BAD_REQUEST);
        }

        UserInfo user = userService.findByUsernameInfo(userInfo.getUsername());

        if(user==null) {
            return new ResponseEntity<>("This user does not exist.", HttpStatus.BAD_REQUEST);
        } else
        {
            userService.addContact(authentication.getName(), userInfo.getUsername());
            return new ResponseEntity<>("User " + userInfo.getUsername() + " was added", HttpStatus.OK);
        }
    }


    @GetMapping(value = "chats")
    public ResponseEntity<List<UserInfo>> getChats(Authentication authentication) {
        List<UserInfo> user = userService.getChats(authentication.getName());

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @GetMapping(value = "chats/{username}")
    public ResponseEntity<List<Message>> getMessagesInChat(Authentication authentication, @PathVariable(name = "username") String username) {
        List<Message> user = messageService.getMessagesInChat(authentication.getName(), username);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "chats/messages")
    public ResponseEntity<String> createMessage(@RequestBody Message message, Authentication authentication) {
        if(message.getText().isEmpty()){
            return new ResponseEntity<>("Message is empty", HttpStatus.BAD_REQUEST);
        }

        if(message.getMsgTo().equals(authentication.getName())){
            return new ResponseEntity<>("You cannot send a message to yourself", HttpStatus.BAD_REQUEST);
        }

        UserInfo user = userService.findByUsernameInfo(message.getMsgTo());

        if(user==null) {
            return new ResponseEntity<>("This user does not exist", HttpStatus.BAD_REQUEST);
        } else {
            messageService.createMessage(message.getText(), authentication.getName(), message.getMsgTo());
            return new ResponseEntity<>("Message was send", HttpStatus.OK);
        }
    }

    @PutMapping(value = "myprofile")
    public void changeSurname(@RequestBody UserInfo userInfo, Authentication authentication) {
        if(userInfo.getSurname()!=null){
            userService.changeSurname(userInfo.getSurname(), authentication.getName());
        }
        else
        {
            userService.changeFisrtname(userInfo.getFirstname(), authentication.getName());
        }
    }


    @PostMapping(value = "myprofile")
    public void addSurname(@RequestBody UserInfo userInfo, Authentication authentication) {
        if (userInfo.getSurname()!=null) {
            userService.addSurname(userInfo.getSurname(), authentication.getName());
        }
        else
        {
            userService.addFirstname(userInfo.getFirstname(), authentication.getName());
        }


    }

    @GetMapping(value = "/image/{username}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType( @PathVariable(name = "username")String username) throws IOException {
        FileInputStream in = new FileInputStream("resources/images/" + username + ".jpg");
        byte[] result = IOUtils.toByteArray(in);
        in.close();
        return result;
    }

}
