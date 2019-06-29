package com.wl.wlp2ploansystem.infrastructures.workflow.vo;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode()
public class WF_AgentUserInfo {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public WF_AgentUserInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public WF_AgentUserInfo setName(String name) {
        this.name = name;
        return this;
    }

    public WF_AgentUserInfo(String id, String name) {
        this.id = id;

        this.name = name;
    }
}
