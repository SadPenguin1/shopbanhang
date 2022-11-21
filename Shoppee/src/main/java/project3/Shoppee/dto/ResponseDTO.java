package project3.Shoppee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
	private int code;
	private String msg ;
	
	
	@JsonInclude(Include.NON_NULL)
	private Integer totalPage;
	
	@JsonInclude(Include.NON_NULL)
	private Long count;
	
	private Object data;
	
	public ResponseDTO(int code, String msg) {
		super();
		this.code =code;
		this.msg=msg;
		
	}

	
	
}
