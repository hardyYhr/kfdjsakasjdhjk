package com.orcus.hha_report_manager.controller;

import com.orcus.hha_report_manager.model.Message;
import com.orcus.hha_report_manager.model.Reply;
import com.orcus.hha_report_manager.repository.MessageRepository;
import com.orcus.hha_report_manager.repository.ReplyRepository;
import com.orcus.hha_report_manager.security.beans.HTTPRequestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    private HTTPRequestUser httpRequestUser;

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(@RequestParam(required = false) String department, String username) {
        try {
            List<Message> messages = new ArrayList<Message>();

            if (department == null && username == null)
                messageRepository.findAll().forEach(messages::add);
            else if (department != null)
                messageRepository.findByDepartmentContains(department).forEach(messages::add);
            else if (username != null)
                messageRepository.findByUsername(username).forEach(messages::add);

            if (messages.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable("id") long id) {
        Optional<Message> messageData = messageRepository.findById(id);

        if (messageData.isPresent()) {
            return new ResponseEntity<>(messageData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        var employee = httpRequestUser.getEmployee();
        message.setUsername(employee.getUsername());
        message.setFirstName(employee.getFirstName());
        message.setLastName(employee.getLastName());

        try {
            Message newMessage = messageRepository
                    .save(new Message(message.getUsername(), message.getFirstName(), message.getLastName(), message.getDepartment(), LocalDateTime.now(), message.getContent()));
            return new ResponseEntity<>(newMessage, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/messages/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable("id") long id, @RequestBody Message message) {
        Optional<Message> messageData = messageRepository.findById(id);

        if (messageData.isPresent()) {
            Message messageToChange = messageData.get();
            if(Objects.nonNull(message.getDepartment())){
                messageToChange.setDepartment(message.getDepartment());
            }
            if(Objects.nonNull(message.getContent())){
                messageToChange.setContent(message.getContent());
            }
            if(Objects.nonNull(message.getReplies())){
                messageToChange.setReplies(message.getReplies());
            }
            messageToChange.setTimestamp(LocalDateTime.now());
            return new ResponseEntity<>(messageRepository.save(messageToChange), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<HttpStatus> deleteMessage(@PathVariable("id") long id) {
        try {
            messageRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/messages")
    public ResponseEntity<HttpStatus> deleteAllMessages() {
        try {
            messageRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //Replies to Messages (like Messages, but without their own Replies i.e. not Message -> Replies -> Replies to Replies)

    @PostMapping("/messages/{id}/replies")
    public ResponseEntity<Message> replyToMessage(@PathVariable("id") long id, @RequestBody Reply reply) {
        Optional<Message> messageData = messageRepository.findById(id);
        reply.setTimestamp(LocalDateTime.now());
        if (messageData.isPresent()) {
            var employee = httpRequestUser.getEmployee();
            reply.setUsername(employee.getUsername());
            reply.setFirstName(employee.getFirstName());
            reply.setLastName(employee.getLastName());
            Message parent = messageData.get();
            parent.getReplies().add(reply);
            return new ResponseEntity<>(messageRepository.save(parent), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/messages/replies/{id}")
    public ResponseEntity<Reply> updateReply(@PathVariable("id") long id, @RequestBody Reply reply) {
        Optional<Reply> replyData = replyRepository.findById(id);

        if (replyData.isPresent()) {
            Reply replyToChange = replyData.get();
            if(Objects.nonNull(reply.getDepartment())){
                replyToChange.setDepartment(reply.getDepartment());
            }
            if(Objects.nonNull(reply.getContent())){
                replyToChange.setContent(reply.getContent());
            }
            replyToChange.setTimestamp(LocalDateTime.now());
            return new ResponseEntity<>(replyRepository.save(replyToChange), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/messages/replies/{id}")
    public ResponseEntity<HttpStatus> deleteReply(@PathVariable("id") long id) {
        try {
            replyRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}