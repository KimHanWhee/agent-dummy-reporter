package kr.standard.ums.dto.Report;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RcsImageResponse {
    private String chatbotId; // 챗봇 아이디

    private String fileId; // 파일 이름

    private String userCode; //내부적으로 생성한 file Server 내부에 저장되어지는 이름

    private String expireTime; // 만료 시간

    private String result; // 결과

    private String resultMessage; // 결과 메시지
}
