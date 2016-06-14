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
@Table(name = "Logs2")
@NamedQueries({@NamedQuery(name = "LogEntity.findAll", query = "SELECT l FROM LogEntity l")})
public class LogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(columnDefinition = "timestamp")
    private Timestamp data;

    private String userName = "null";

    private String userIp;


    @NotNull
    @Enumerated(EnumType.STRING)
    private LogPriority priority;

    @NotNull
    @Column(columnDefinition = "mediumtext")
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

    public String getUserName() {
        return userName;
    }

    public LogEntity setUserName(String userName) {
        this.userName = userName;
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
                ", userName=" + userName +
                ", userIp='" + userIp + '\'' +
                ", priority=" + priority +
                ", message='" + message + '\'' +
                '}';
    }
}
