package org.example;

import java.io.Serializable;
import java.net.InetSocketAddress;

public class Member implements Serializable {

    private static final long serialVersionUID = 8387950590016941525L;

    /**
     * The member address in the form IP:port
     * Similar to the toString in {@link InetSocketAddress}
     */
    private String address;

    private int heartbeat;

    private transient TimeoutTimer timeoutTimer;

    public Member(String address, int heartbeat, Client client, int t_cleanup) {
        this.address = address;
        this.heartbeat = heartbeat;
        this.timeoutTimer = new TimeoutTimer(t_cleanup, client, this);
    }

    public void startTimeoutTimer() {
        this.timeoutTimer.start();
    }

    public void resetTimeoutTimer() {
        this.timeoutTimer.reset();
    }

    public String getAddress() {
        return address;
    }

    public int getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }

    @Override
    public String toString() {
        return "Member [address=" + address + ", heartbeat=" + heartbeat + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((address == null) ? 0 : address.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Member other = (Member) obj;
        if (address == null) {
            if (other.address != null) {
                return false;
            }
        } else if (!address.equals(other.address)) {
            return false;
        }
        return true;
    }
}