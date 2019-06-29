package com.wl.wlp2ploansystem.infrastructures.workflow.editor.controller;

import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.workflow.editor.service.EditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/wf")
public class EditorController {

    @Autowired
    EditorService editorService;


    @GetMapping("/editor/stencilset")
    @Log(disabled = true)
    public Object getStencilset() throws IOException {
       return editorService.getStencilset();
    }


    @GetMapping(value = "/model/{modelId}/json")
    @Log(disabled = true)
    public Object getEditorJson(@PathVariable(value ="modelId" )  String modelId)  throws IOException {
        return editorService.getEditorJson(modelId);
    }


    @PutMapping("/model/{modelId}/save")
    @Log(disabled = true)
    public void saveModel(@PathVariable(value ="modelId" ) String modelId, String name, String description,
                          String json_xml, String svg_xml)  throws IOException {
        editorService.saveModel(modelId, name, description, json_xml, svg_xml);
    }
}
