package com.micro_service_librerie.librerie.Model;

import java.util.Date;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErreurMessage {
    
    Date time;
    Integer code;
    Object errors;
    
   
}
