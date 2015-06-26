package io.github.daviddenton.finaglecircuit

import io.github.daviddenton.finaglecircuit.CircuitStatus.CircuitStatus

case class CircuitStateChange(circuitName: CircuitName, state: CircuitStatus)