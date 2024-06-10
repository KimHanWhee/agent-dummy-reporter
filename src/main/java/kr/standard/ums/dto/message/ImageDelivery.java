package kr.standard.ums.dto.message;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDelivery {
    private String imgPath;
    private String chatbotId;
    private String fileId;
    private String userCode;
}
