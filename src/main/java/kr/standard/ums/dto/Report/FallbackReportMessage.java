package kr.standard.ums.dto.Report;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FallbackReportMessage {

    private String cmpMsgId; //20자리
    private String srcMsgId;
    private String umsMsgId; //20자리
    // 처음 보냈을 시 채널을 받는다. (후일 RCS, KKO에 대한 재처리를 같은 컨트롤러에서 처리할 시 분기처리를 위함)
    private String channel; //10자리
    private String fbChannel;
    //처리 결과 코드
    private String fbResultCode; //5자리
    //처리 결과 메시지
    private String fbResultMessage;//40자리
    //이동통신사
    private String fbServiceProvider;//10자리
    private String fbRptRcvDttm;
    // MCMP에서 재처리 하여 submit Ack를 받은 시간
    private String fbPfmSndDttm;
    private String fbPfmRcvDttm;
}
