package com.tunebrains.gradle

import org.gradle.api.Project

import javax.inject.Inject

/**
 * Created by alex on 4/24/16.
 */
class FlattenerExtension {
    final Project project;
    boolean enabled
    String root
    @Inject
    FlattenerExtension(Project pProject) {
        this.project = pProject
    }
}
