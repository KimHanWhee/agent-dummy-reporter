package kr.standard.ums.dto;

import lombok.*;

import java.util.List;

@Setter@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Fallback {
    // FallBack 처리 위한 데이터
    private String type; //재처리 할 메세지 채널
    private String subject; // 재처리 시 제목
    private String content; // 재처리 시 본문
    private List<String> fileIds; // 재처리 시 전송 파일
    private String originCode; // 재처리 시 입력 될 고객사 최초 발신자 코드
}
