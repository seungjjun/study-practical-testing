package prac.pizzashop.spring.api.service.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import prac.pizzashop.spring.client.mail.MailSendClient;
import prac.pizzashop.spring.domain.history.mail.MailSendHistory;
import prac.pizzashop.spring.domain.history.mail.MailSendHistoryRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private MailSendClient mailSendClient;

//    @Spy  // 실제 객체를 기반으로 스파이 객체를 생성
//    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService;

    @DisplayName("메일 전송 테스트")
    @Test
    void sendMail() {
        // Given
        // Mockito를 사용하여 메일 전송
//        Mockito.when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString())).thenReturn(true);

        // BDDMockito를 사용하여 메일 전송
        BDDMockito.given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString())).willReturn(true);

        // Spy 객체를 사용하여 실제 메일 전송 로직을 호출
//        doReturn(true)
//          .when(mailSendClient)
//            .sendEmail(anyString(), anyString(), anyString(), anyString());

        // When
        boolean result = mailService.sendMail("", "", "", "");

        // Then
        assertThat(result).isTrue();
        verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
    }

}
