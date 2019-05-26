package si.francebevk.dto

import si.francebevk.db.enums.AuthorizedPersonType

class AuthorizedPerson(
    val name: String?,
    val type: AuthorizedPersonType?
)