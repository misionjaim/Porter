package cu.control.queue.repository.dataBase.entitys.payload.params

import androidx.annotation.Keep

@Keep
class ParamDeleteMember (
    val person_id: Array<String>,
    val deleted_date: Long
): Param()