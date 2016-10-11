package com.ippon.attendance.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ippon.attendance.domain.Attendee;

import com.ippon.attendance.repository.AttendeeRepository;
import com.ippon.attendance.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Attendee.
 */
@RestController
@RequestMapping("/api")
public class AttendeeResource {

    private final Logger log = LoggerFactory.getLogger(AttendeeResource.class);
        
    @Inject
    private AttendeeRepository attendeeRepository;

    /**
     * POST  /attendees : Create a new attendee.
     *
     * @param attendee the attendee to create
     * @return the ResponseEntity with status 201 (Created) and with body the new attendee, or with status 400 (Bad Request) if the attendee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/attendees",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Attendee> createAttendee(@RequestBody Attendee attendee) throws URISyntaxException {
        log.debug("REST request to save Attendee : {}", attendee);
        if (attendee.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("attendee", "idexists", "A new attendee cannot already have an ID")).body(null);
        }
        Attendee result = attendeeRepository.save(attendee);
        return ResponseEntity.created(new URI("/api/attendees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("attendee", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /attendees : Updates an existing attendee.
     *
     * @param attendee the attendee to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated attendee,
     * or with status 400 (Bad Request) if the attendee is not valid,
     * or with status 500 (Internal Server Error) if the attendee couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/attendees",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Attendee> updateAttendee(@RequestBody Attendee attendee) throws URISyntaxException {
        log.debug("REST request to update Attendee : {}", attendee);
        if (attendee.getId() == null) {
            return createAttendee(attendee);
        }
        Attendee result = attendeeRepository.save(attendee);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("attendee", attendee.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attendees : get all the attendees.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of attendees in body
     */
    @RequestMapping(value = "/attendees",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Attendee> getAllAttendees() {
        log.debug("REST request to get all Attendees");
        List<Attendee> attendees = attendeeRepository.findAll();
        return attendees;
    }

    /**
     * GET  /attendees/:id : get the "id" attendee.
     *
     * @param id the id of the attendee to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the attendee, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/attendees/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Attendee> getAttendee(@PathVariable Long id) {
        log.debug("REST request to get Attendee : {}", id);
        Attendee attendee = attendeeRepository.findOne(id);
        return Optional.ofNullable(attendee)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /attendees/:id : delete the "id" attendee.
     *
     * @param id the id of the attendee to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/attendees/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAttendee(@PathVariable Long id) {
        log.debug("REST request to delete Attendee : {}", id);
        attendeeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("attendee", id.toString())).build();
    }

}
