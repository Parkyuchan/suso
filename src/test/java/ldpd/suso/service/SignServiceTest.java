package ldpd.suso.service;
import ldpd.suso.entity.Sign;
import ldpd.suso.repository.SignRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignServiceTest {

    @Mock
    private SignRepository signRepository;

    @InjectMocks
    private SignService signService;

    @Test
    void testRegisterSign() throws Exception {
        // Given
        Sign sign = new Sign();
        sign.setId(1);
        sign.setTitle("title");
        sign.setSign_desc("desc");
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);

        // When
        signService.registerSign(sign, file);

        // Then
        verify(signRepository, times(1)).save(sign);
    }

    @Test
    void testRegisterSign_isEmpty_file() throws Exception {
        // Given
        Sign sign = new Sign();
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        // When
        signService.registerSign(sign, file);

        // Then
        verify(signRepository, never()).save(sign);
    }

    @Test
    void testSignSearchList() {
        // Given
        String searchKeyword = "keyword";
        Pageable pageable = mock(Pageable.class);
        Page<Sign> expectedPage = mock(Page.class);
        when(signRepository.findByTitleContaining(searchKeyword, pageable)).thenReturn(expectedPage);

        // When
        Page<Sign> actualPage = signService.signSearchList(searchKeyword, pageable);

        // Then
        assertSame(expectedPage, actualPage);
    }

    @Test
    void testSignDelete() {
        // Given
        Integer id = 1;
        Sign sign = new Sign();
        sign.setFilepath("/files/test.jpg");
        when(signRepository.findById(id)).thenReturn(Optional.of(sign));

        // When
        signService.signDelete(id);

        // Then
        verify(signRepository, times(1)).deleteById(id);
    }
}
