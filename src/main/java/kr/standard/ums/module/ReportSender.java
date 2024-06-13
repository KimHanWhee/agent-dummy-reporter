package kr.standard.ums.module;

import kr.standard.ums.Util.Util;
import kr.standard.ums.dto.Report.FallbackReportMessage;
import kr.standard.ums.dto.Report.RcsImageResponse;
import kr.standard.ums.dto.Report.ReportMessage;
import kr.standard.ums.dto.message.ImageDelivery;
import kr.standard.ums.dto.message.MessageDelivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;  

@Slf4j
@Service
public class ReportSender {
    private WebClient webClient;

    @PostConstruct
    public void initReporter() {
        String reportUrl = "http://192.168.50.24:9913";
//        String reportUrl = "http://211.238.138.208:9960";
        webClient = WebClient.create(reportUrl);
    }

    public Mono<Boolean> send(MessageDelivery messageDelivery) {
        return webClient.post()
                .uri("/v1/message/report")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(makeReportMessage(messageDelivery)), ReportMessage.class)
                .retrieve()
                // Reponse Body가 필요하지 않을 경우 사용
                .toBodilessEntity()
                .then(Mono.just(true))
                .onErrorResume(e -> {
                    log.error("Send Report Failed...!!! Retry Send Report!");

                    return Mono.just(false);
                });
    }

    public Mono<Boolean> fallbackSend(MessageDelivery messageDelivery) {
        return webClient.post()
                .uri("/v1/message/report/fallback")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(makeFallbackReportMessage(messageDelivery)), FallbackReportMessage.class)
                .retrieve()
                // Reponse Body가 필요하지 않을 경우 사용
                .toBodilessEntity()
                .then(Mono.just(true))
                .onErrorResume(e -> {
                    log.error("Error Occurred while send fallback report :", e);

                    return Mono.just(false);
                });
    }

    public Mono<Boolean> imageSend(ImageDelivery imageDelivery) {
        return webClient.post()
                .uri("/v1/rcs/report/file")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(makeRcsImageReportMessage(imageDelivery)), RcsImageResponse.class)
                .retrieve()
                .toBodilessEntity()
                .then(Mono.just(true))
                .onErrorResume(e -> {
                    log.error("Error Occurred while send RCS FILE report : ", e);

                    return Mono.just(false);
                });
    }

    public ReportMessage makeReportMessage(MessageDelivery messageDelivery) {
        return ReportMessage.builder()
                .srcMsgId(messageDelivery.getSrcMsgId())
                .umsMsgId(messageDelivery.getUmsMsgId())
                .channel(messageDelivery.getChannel())
                .serviceProvider("SKT")
                .resultCode("-100")
                .resultMessage("E_SEND")
                .pfmRcvDttm(Util.getCurrentTime())
                .pfmSndDttm(Util.getCurrentTime())
                .build();
    }

    public FallbackReportMessage makeFallbackReportMessage(MessageDelivery messageDelivery) {
        return FallbackReportMessage.builder()
                .srcMsgId(messageDelivery.getSrcMsgId())
                .umsMsgId(messageDelivery.getUmsMsgId())
                .channel(messageDelivery.getChannel())
                .fbChannel(messageDelivery.getFallback().getType())
                .fbResultCode("-100")
                .fbResultMessage("전송 성공")
                .fbPfmRcvDttm(Util.getCurrentTime())
                .fbPfmSndDttm(Util.getCurrentTime())
                .fbRptRcvDttm(Util.getCurrentTime())
                .fbServiceProvider("SKT")
                .build();
    }

   public RcsImageResponse makeRcsImageReportMessage(ImageDelivery imageDelivery) {
        return RcsImageResponse.builder()
                .chatbotId(imageDelivery.getChatbotId())
                .fileId(imageDelivery.getFileId())
                .userCode(imageDelivery.getUserCode())
                .expireTime(Util.getTimeAfter1Hour())
                .result("-100")
                .resultMessage("등록 성공")
                .build();
   }
}
