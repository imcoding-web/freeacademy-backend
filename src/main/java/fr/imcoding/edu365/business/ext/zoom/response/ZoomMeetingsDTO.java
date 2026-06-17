package fr.imcoding.edu365.business.ext.zoom.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ZoomMeetingsDTO {
  @JsonProperty("page_count")
  private int page;
  @JsonProperty("page_size")
  private int pageSize;
  @JsonProperty("total_records")
  private int total;

  @JsonProperty("meetings")
  List<ZoomMeetingObjectDTO> meetings;
}
