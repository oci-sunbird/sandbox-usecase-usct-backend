package global.govstack.usct.controller.dto;

import global.govstack.usct.model.Consent;
import global.govstack.usct.types.ConsentStatus;
import java.time.LocalDateTime;
import lombok.Getter;

public class ConsentDto {

  private Integer id;

  @Getter private ConsentStatus status;
  @Getter private LocalDateTime dataTime;

  public ConsentDto() {}

  public ConsentDto(Integer id, ConsentStatus status, LocalDateTime dataTime) {
    this.id = id;
    this.status = status;
    this.dataTime = dataTime;
  }

  public ConsentDto(Consent consent) {
    this.id = consent.getId();
    this.status = consent.getConsentStatus();
    this.dataTime = consent.getDateTime();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setStatus(ConsentStatus status) {
    this.status = status;
  }

  public void setDataTime(LocalDateTime dataTime) {
    this.dataTime = dataTime;
  }
}
