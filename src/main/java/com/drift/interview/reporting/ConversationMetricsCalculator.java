package com.drift.interview.reporting;

import com.drift.interview.model.Conversation;
import com.drift.interview.model.ConversationResponseMetric;
import com.drift.interview.model.Message;
import java.util.List;

public class ConversationMetricsCalculator {
  public ConversationMetricsCalculator() {}

  /**
   * Returns a ConversationResponseMetric object which can be used to power data visualizations on the front end.
   */
  ConversationResponseMetric calculateAverageResponseTime(Conversation conversation) {
    List<Message> messages = conversation.getMessages();
    //Average response time
    long responseTime = 0;
    long average = 0;
    int counterMessage = 0;
    boolean end_user = false;
    for (Message message : messages) {
      //for each message, keep track of who is the team member and who is the user
      boolean current = message.isTeamMember();
      //if it is the user, then keep searching
      if (!current && !end_user) {
        average -= message.getCreatedAt();
        end_user = true;
      }
      //if it is the team member, then get the response time and add it to the variable keeping the time
      else if (current && end_user) {
        average += message.getCreatedAt();
        counterMessage++;
        end_user = false;
      }
    }
    //once is done finding the times, then calculate the average
    //used an if statement instead of a try block because try blocks can sometimes be expensive
    if(counterMessage > 0) {
      responseTime = average / counterMessage;
    }
    return ConversationResponseMetric.builder()
        .setConversationId(conversation.getId())
        .setAverageResponseMs(responseTime)
        .build();
  }
}
