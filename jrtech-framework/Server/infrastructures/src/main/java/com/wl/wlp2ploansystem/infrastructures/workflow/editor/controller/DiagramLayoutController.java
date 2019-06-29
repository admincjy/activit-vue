package com.wl.wlp2ploansystem.infrastructures.workflow.editor.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.workflow.editor.service.DiagramLayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController

public class DiagramLayoutController extends DiagramLayoutBaseController {

    @Autowired
    DiagramLayoutService diagramLayoutService;


    @GetMapping("/process-instance/{processInstanceId}/highlights")
    @Log(disabled = true)
    public ObjectNode getHighlighted(@PathVariable String processInstanceId) {
        return diagramLayoutService.getHighlighted(processInstanceId);
    }


    @GetMapping("/process-instance/{processInstanceId}/diagram-layout")
    @Log(disabled = true)
    public ObjectNode getDiagramByPid(@PathVariable String processInstanceId) {
        return getDiagramNode(processInstanceId, null);
    }


    @GetMapping("/process-definition/{processDefinitionId}/diagram-layout")
    @Log(disabled = true)
    public ObjectNode getDiagramByPdid(@PathVariable String processDefinitionId) {
        return getDiagramNode(null, processDefinitionId);
    }
}
