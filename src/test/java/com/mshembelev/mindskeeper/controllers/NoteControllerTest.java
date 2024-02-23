package com.mshembelev.mindskeeper.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mshembelev.mindskeeper.dto.note.CreateNoteRequest;
import com.mshembelev.mindskeeper.models.NoteModel;
import com.mshembelev.mindskeeper.models.Role;
import com.mshembelev.mindskeeper.models.UserModel;
import com.mshembelev.mindskeeper.security.JwtAuthenticationFilter;
import com.mshembelev.mindskeeper.services.NoteService;
import com.mshembelev.mindskeeper.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = NoteController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class NoteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;
    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private NoteModel note;
    private CreateNoteRequest noteRequest;
    private UserModel user;

    @BeforeEach
    public void init(){
        note = NoteModel.builder().userId(0L).text("Some text").title("Title").id(0L).build();
        noteRequest = CreateNoteRequest.builder().text("Some text").title("Title").build();
        user = UserModel.builder().id(0L).username("Test").password("wdwdwda").role(Role.ROLE_USER).email("dawddw@mail.ru").groupId(0L).build();
    }

    @Test
    public void NoteController_CreateNote_ReturnCreated() throws Exception{
        Long userId = 0L;
        given(noteService.createNote(noteRequest, userId)).willAnswer((invocation -> note));
        given(userService.getCurrentUser()).willAnswer((invocation -> user));
        ResultActions response = mockMvc.perform(post("/note/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteRequest)));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

}
