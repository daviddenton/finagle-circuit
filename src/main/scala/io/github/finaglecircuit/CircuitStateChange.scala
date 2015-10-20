package io.github.finaglecircuit

import io.github.finaglecircuit.CircuitStatus.CircuitStatus

case class CircuitStateChange(circuitName: CircuitName, state: CircuitStatus)