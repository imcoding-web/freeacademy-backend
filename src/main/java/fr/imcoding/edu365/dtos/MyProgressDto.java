package fr.imcoding.edu365.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyProgressDto {
  private List<String> visitedCourseUuids;
  private List<String> completedCourseUuids;
}
