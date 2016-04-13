package net.chat.logging;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Mariusz Gorzycki
 * @since 30.03.2016
 */
@Entity
@NamedQueries({@NamedQuery(name = "LogEntity.findAll", query = "SELECT l FROM LogEntity l")})
public class LogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Timestamp data;

    private Long userId;

    private String userIp;

    @NotNull
    private LogPriority priority;

    @NotNull
    private String message;

    public static LogEntity withCurrentDate() {
        return new LogEntity().setData(new Timestamp(System.currentTimeMillis()));
    }

    public Long getId() {
        return id;
    }

    public LogEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public Timestamp getData() {
        return data;
    }

    public LogEntity setData(Timestamp data) {
        this.data = data;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public LogEntity setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getUserIp() {
        return userIp;
    }

    public LogEntity setUserIp(String userIp) {
        this.userIp = userIp;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public LogEntity setMessage(String message) {
        this.message = message;
        return this;
    }

    public LogPriority getPriority() {
        return priority;
    }

    public LogEntity setPriority(LogPriority priority) {
        this.priority = priority;
        return this;
    }

    @Override public String toString() {
        return "Log{" +
                "data=" + data +
                ", userId=" + userId +
                ", userIp='" + userIp + '\'' +
                ", priority=" + priority +
                ", message='" + message + '\'' +
                '}';
    }
}
