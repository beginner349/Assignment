package sg.com.nphc.Assignment.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class EmployeeServiceImplTest {

    @BeforeEach
    public void beforeEach() {

    }

    @AfterEach
    public void afterEach() {

    }

    @Test
    public void testSave() throws IOException {
        InputStream is = getClass().getResourceAsStream("/sample.csv");
        MultipartFile multipartFile = new MockMultipartFile("file", "sample.csv", "text/csv", is);
    }


}