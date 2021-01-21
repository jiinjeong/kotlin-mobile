package hui.ait.finalproject.data

data class EmergencyResult(val Fire: FireNum?, val Ambulance: AmbulanceNum?, val Police: PoliceNum?)

data class FireNum(val All: Number?)
data class AmbulanceNum(val All: Number?)
data class PoliceNum(val All: Number?)
