package fr.imcoding.edu365.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 30/07/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {

  private List<T> items;
  private long count;
}
