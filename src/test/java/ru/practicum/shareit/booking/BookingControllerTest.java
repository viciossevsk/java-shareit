package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ValidationException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    private final LocalDateTime start = LocalDateTime.now();
    private final LocalDateTime end = LocalDateTime.now().plusMinutes(1);
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService mockBookingService;
    long bookerId = 1L;
    boolean isApproved = true;
    long bookingId = 1L;
    private Integer from = 0;
    private Integer size = 5;
    @Autowired
    private MockMvc mvc;
    private BookingDto bookingDtoRequest = BookingDto.builder()
            .itemId(1L)
            .start(start)
            .end(end)
            .build();
    private BookingDto bookingDtoResponse = BookingDto.builder()
            .id(1L)
            .itemId(1L)
            .start(start)
            .end(end)
            .status(BookingStatus.WAITING)
            .build();

    @Test
    void createBooking_thenRequest_whenResponseOk() throws Exception {

        when(mockBookingService.createBooking(anyLong(), any(BookingDto.class)))
                .thenReturn(bookingDtoResponse);

        mvc.perform(post("/bookings")
                            .content(mapper.writeValueAsString(bookingDtoRequest))
                            .header("X-Sharer-User-Id", bookerId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.itemId", is(bookingDtoResponse.getItemId().intValue())))
                .andExpect(jsonPath("$.start",
                                    is(bookingDtoResponse.getStart().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(bookingDtoResponse.getEnd().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())));

        verify(mockBookingService).createBooking(anyLong(), any(BookingDto.class));
    }

    @Test
    void createBooking_thenStartDateInPast_whenValidationException() throws Exception {

        when(mockBookingService.createBooking(anyLong(), any(BookingDto.class)))
                .thenThrow(new ValidationException("Booking start time after end time"));

        mvc.perform(post("/bookings")
                            .content(mapper.writeValueAsString(bookingDtoRequest))
                            .header("X-Sharer-User-Id", bookerId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(mockBookingService).createBooking(anyLong(), any(BookingDto.class));
    }

    @Test
    void approvingBooking_thenIsApproved_whenStatusApproved() throws Exception {
        bookingDtoResponse.setStatus(BookingStatus.APPROVED);

        when(mockBookingService.approvingBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDtoResponse);

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                            .header("X-Sharer-User-Id", bookerId)
                            .param("approved", String.valueOf(isApproved))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(BookingStatus.APPROVED.toString())));
    }

    @Test
    void getBooking_thenRequest_whenResponseOk() throws Exception {

        when(mockBookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingDtoResponse);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                            .header("X-Sharer-User-Id", bookerId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(mockBookingService).getBooking(anyLong(), anyLong());

    }

    @Test
    void getBookerStatistics_checkParam() throws Exception {

        when(mockBookingService.getBookerStatistics(anyLong(), anyString(), eq(from), eq(size)))
                .thenReturn(List.of(bookingDtoResponse));

        mvc.perform(get("/bookings")
                            .param("state", "ALL")
                            .param("from", String.valueOf(from))
                            .param("size", String.valueOf(size))
                            .header("X-Sharer-User-Id", bookerId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(mockBookingService, Mockito.times(1)).getBookerStatistics(anyLong(), eq("ALL"), eq(from), eq(size));
    }

    @Test
    void getOwnerStatistics_thenRequest_whenResponseOk() throws Exception {

        when(mockBookingService.getOwnerStatistics(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse));


        mvc.perform(get("/bookings/owner")
                            .param("state", "ALL")
                            .param("from", String.valueOf(from))
                            .param("size", String.valueOf(size))
                            .header("X-Sharer-User-Id", bookerId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(mockBookingService, never()).getBooking(anyLong(), anyLong());
    }

}
