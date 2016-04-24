package com.tunebrains.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class SourceSetFlattenerPlugin implements Plugin<Project> {
    String[] FILTERS = ["values", "drawable", "mipmap", "menu", "anim", "animator", "color", "xml", "transition", "interpolator", "layout", "raw"]

    void apply(Project project) {
        def flattenerConfig = project.extensions.create("flattener", FlattenerExtension, project)
        project.afterEvaluate {

            project.android.sourceSets.all { sourceSet ->

            }
            project.android.applicationVariants.all { variant ->
                variant.sourceSets.each { ss ->
                    List<String> srcs = buildSources(project, variant, ss, flattenerConfig)
                    ss.res({ obj ->
                        obj.srcDirs(srcs)
                    })

                }

            }

        }
    }

    List<String> buildSources(Project pProject, Object variant, Object pSourceSet, FlattenerExtension pFlattenerExtension) {
        List<String> dirs = new LinkedList<>()
        pSourceSet.getRes().each { r ->
            r.getSrcDirs().each { rd ->
                File newDir = new File(rd.getParent(), pFlattenerExtension.getRoot());
                extractFlatDir(dirs, newDir)
            }

        }
        dirs
    }

    def extractFlatDir(List<String> pStrings, File pRoot) {
        File[] root = pRoot.listFiles();
        //if there is no files, expect to create later
        if (root == null || root.length == 0) {
            pStrings.add(pRoot.getAbsolutePath())
            return;
        }
        boolean skip = false
        for (File lFile : root) {
            //if directory has one of android resource directories, skip it
            FILTERS.each { v ->
                if (lFile.isDirectory() && lFile.getName().startsWith(v))
                    skip = true
            }
        }

        if (skip)
            pStrings.add(pRoot.getAbsolutePath())
        else {
            for (File lFile : root) {
                if (pRoot.isDirectory()) {
                    extractFlatDir(pStrings, lFile)
                }
            }

        }
    }
}
