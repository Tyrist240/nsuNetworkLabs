package domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Header {

    private Integer filenameSize;

    private String filename;

    private Long fileSize;

}
