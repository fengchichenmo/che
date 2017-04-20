/*******************************************************************************
 * Copyright (c) 2012-2017 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.workspace.infrastructure.docker.old.agents;

import org.eclipse.che.api.agent.shared.model.Agent;
import org.eclipse.che.api.core.model.workspace.config.Command;
import org.eclipse.che.api.workspace.server.model.impl.CommandImpl;
import org.eclipse.che.api.workspace.server.spi.InfrastructureException;
import org.eclipse.che.workspace.infrastructure.docker.model.DockerService;

import static java.lang.String.format;

/**
 * Verifies if agent started a process with specific name.
 * It is an indicator that process had been finished.
 *
 * @author Anatoliy Bazko
 */
public class ProcessIsLaunchedChecker implements AgentLaunchingChecker {

    private static final String CHECK_COMMAND = "command -v pidof >/dev/null 2>&1 && {\n" +
                                                "    pidof %1$s >/dev/null 2>&1 && echo 0 || echo 1\n" +
                                                "} || {\n" +
                                                "    ps -fC %1$s >/dev/null 2>&1 && echo 0 || echo 1\n" +
                                                "}";
    private final String processNameToWait;
    private       long   counter;

    public ProcessIsLaunchedChecker(String processNameToWait) {
        this.processNameToWait = processNameToWait;
    }

    @Override
    public boolean isLaunched(Agent agent, /*DockerProcess process, */DockerService machine) throws
                                                                                         InfrastructureException {
            Command command = new CommandImpl(format("Wait for %s, try %d", agent.getId(), ++counter),
                                          format(CHECK_COMMAND, processNameToWait),
                                          "test");

//        try (ListLineConsumer lineConsumer = new ListLineConsumer()) {
//            InstanceProcess waitProcess = machine.createProcess(command, null);
//            waitProcess.start(lineConsumer);
//            return lineConsumer.getText().endsWith("[STDOUT] 0");
//        } catch (ConflictException e) {
//            throw new MachineException(e.getServiceError());
//        }
        return true;
    }
}
