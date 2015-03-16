package net.moznion.donovan;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represent basically API response.
 * 
 * @author moznion
 *
 */
@EqualsAndHashCode
@ToString
public class BasicAPIResponse {
  private int code;
  private List<String> messages;

  /**
   * Make successfully (200) API response without any messages.
   */
  public BasicAPIResponse() {
    this.code = 200;
    this.messages = Collections.emptyList();
  }

  /**
   * Make API response with message.
   * 
   * @param code Status code
   * @param message Response message
   */
  public BasicAPIResponse(int code, String message) {
    this.code = code;
    this.messages = Arrays.asList(message);
  }

  /**
   * Make API response with messages.
   * 
   * @param code Status code
   * @param messages Response messages
   */
  public BasicAPIResponse(int code, List<String> messages) {
    this.code = code;
    this.messages = messages;
  }

  /**
   * Get status code of API response.
   * 
   * @return Status code
   */
  public int getCode() {
    return this.code;
  }

  /**
   * Set status code for API response.
   * 
   * @param code Status code
   */
  public void setCode(int code) {
    this.code = code;
  }

  /**
   * Get API response messages.
   * 
   * @return API response messages
   */
  public List<String> getMessages() {
    return this.messages;
  }

  /**
   * Set messages for API response.
   * 
   * @param messages API response messages
   */
  public void setMessages(List<String> messages) {
    this.messages = messages;
  }
}
