package cu.control.queue.repository.dataBase.entitys.payload.params

import androidx.annotation.Keep
import cu.control.queue.repository.dataBase.entitys.payload.Person

@Keep
class ParamDeleteMember (
    val person: ArrayList<Person>
): Param()