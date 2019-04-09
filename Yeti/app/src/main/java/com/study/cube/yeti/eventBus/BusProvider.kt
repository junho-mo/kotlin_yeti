package com.study.cube.yeti.eventBus

// With data
data class BusProvider(val fragmentName: String?,
                       var eventKind: EventKind,
                       var param1: String? = null,
                       var tabIndex: Int? = null,
                       var project: Project? = null)
{
    data class Project(var projectName:String? = null
                       ,var projectId:String? = null)
        {
            data class Task(var taskName:String? = null
                            ,var taskId:String? = null)
            {
                data class ToDo(var todoName:String? = null
                                ,var todoId:String? = null)
                data class Calendar(var date:String? = null)
            }
        }
}
