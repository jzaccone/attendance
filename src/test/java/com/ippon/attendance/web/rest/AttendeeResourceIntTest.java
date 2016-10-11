package com.ippon.attendance.web.rest;

import com.ippon.attendance.AttendanceApp;

import com.ippon.attendance.domain.Attendee;
import com.ippon.attendance.repository.AttendeeRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AttendeeResource REST controller.
 *
 * @see AttendeeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AttendanceApp.class)
public class AttendeeResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";

    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    @Inject
    private AttendeeRepository attendeeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAttendeeMockMvc;

    private Attendee attendee;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AttendeeResource attendeeResource = new AttendeeResource();
        ReflectionTestUtils.setField(attendeeResource, "attendeeRepository", attendeeRepository);
        this.restAttendeeMockMvc = MockMvcBuilders.standaloneSetup(attendeeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attendee createEntity(EntityManager em) {
        Attendee attendee = new Attendee()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .email(DEFAULT_EMAIL);
        return attendee;
    }

    @Before
    public void initTest() {
        attendee = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttendee() throws Exception {
        int databaseSizeBeforeCreate = attendeeRepository.findAll().size();

        // Create the Attendee

        restAttendeeMockMvc.perform(post("/api/attendees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(attendee)))
                .andExpect(status().isCreated());

        // Validate the Attendee in the database
        List<Attendee> attendees = attendeeRepository.findAll();
        assertThat(attendees).hasSize(databaseSizeBeforeCreate + 1);
        Attendee testAttendee = attendees.get(attendees.size() - 1);
        assertThat(testAttendee.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAttendee.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAttendee.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAttendees() throws Exception {
        // Initialize the database
        attendeeRepository.saveAndFlush(attendee);

        // Get all the attendees
        restAttendeeMockMvc.perform(get("/api/attendees?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(attendee.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getAttendee() throws Exception {
        // Initialize the database
        attendeeRepository.saveAndFlush(attendee);

        // Get the attendee
        restAttendeeMockMvc.perform(get("/api/attendees/{id}", attendee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attendee.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAttendee() throws Exception {
        // Get the attendee
        restAttendeeMockMvc.perform(get("/api/attendees/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttendee() throws Exception {
        // Initialize the database
        attendeeRepository.saveAndFlush(attendee);
        int databaseSizeBeforeUpdate = attendeeRepository.findAll().size();

        // Update the attendee
        Attendee updatedAttendee = attendeeRepository.findOne(attendee.getId());
        updatedAttendee
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME)
                .email(UPDATED_EMAIL);

        restAttendeeMockMvc.perform(put("/api/attendees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAttendee)))
                .andExpect(status().isOk());

        // Validate the Attendee in the database
        List<Attendee> attendees = attendeeRepository.findAll();
        assertThat(attendees).hasSize(databaseSizeBeforeUpdate);
        Attendee testAttendee = attendees.get(attendees.size() - 1);
        assertThat(testAttendee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAttendee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAttendee.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void deleteAttendee() throws Exception {
        // Initialize the database
        attendeeRepository.saveAndFlush(attendee);
        int databaseSizeBeforeDelete = attendeeRepository.findAll().size();

        // Get the attendee
        restAttendeeMockMvc.perform(delete("/api/attendees/{id}", attendee.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Attendee> attendees = attendeeRepository.findAll();
        assertThat(attendees).hasSize(databaseSizeBeforeDelete - 1);
    }
}
