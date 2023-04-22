/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.sql.parser.postgresql.visitor.statement.type;

import org.apache.shardingsphere.sql.parser.api.ASTNode;
import org.apache.shardingsphere.sql.parser.api.visitor.statement.type.DCLSQLVisitor;
import org.apache.shardingsphere.sql.parser.autogen.PostgreSQLStatementParser.AlterRoleContext;
import org.apache.shardingsphere.sql.parser.autogen.PostgreSQLStatementParser.AlterUserContext;
import org.apache.shardingsphere.sql.parser.autogen.PostgreSQLStatementParser.CreateGroupContext;
import org.apache.shardingsphere.sql.parser.autogen.PostgreSQLStatementParser.CreateRoleContext;
import org.apache.shardingsphere.sql.parser.autogen.PostgreSQLStatementParser.CreateUserContext;
import org.apache.shardingsphere.sql.parser.autogen.PostgreSQLStatementParser.DropRoleContext;
import org.apache.shardingsphere.sql.parser.autogen.PostgreSQLStatementParser.DropUserContext;
import org.apache.shardingsphere.sql.parser.autogen.PostgreSQLStatementParser.GrantContext;
import org.apache.shardingsphere.sql.parser.autogen.PostgreSQLStatementParser.PrivilegeClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.PostgreSQLStatementParser.ReassignOwnedContext;
import org.apache.shardingsphere.sql.parser.autogen.PostgreSQLStatementParser.RevokeContext;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.SimpleTableSegment;
import org.apache.shardingsphere.sql.parser.sql.common.value.collection.CollectionValue;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.dcl.PostgreSQLAlterRoleStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.dcl.PostgreSQLAlterUserStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.dcl.PostgreSQLCreateGroupStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.dcl.PostgreSQLCreateRoleStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.dcl.PostgreSQLCreateUserStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.dcl.PostgreSQLDropRoleStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.dcl.PostgreSQLDropUserStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.dcl.PostgreSQLGrantStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.dcl.PostgreSQLReassignOwnedStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.dcl.PostgreSQLRevokeStatement;

import java.util.Collection;
import java.util.Optional;

/**
 * DCL Statement SQL visitor for PostgreSQL.
 */
public final class PostgreSQLDCLStatementSQLVisitor extends PostgreSQLStatementSQLVisitor implements DCLSQLVisitor {
    
    @Override
    public ASTNode visitGrant(final GrantContext ctx) {
        PostgreSQLGrantStatement result = new PostgreSQLGrantStatement();
        Optional<Collection<SimpleTableSegment>> tableSegment = null == ctx.privilegeClause() ? Optional.empty() : getTableFromPrivilegeClause(ctx.privilegeClause());
        tableSegment.ifPresent(optional -> result.getTables().addAll(optional));
        return result;
    }
    
    @Override
    public ASTNode visitRevoke(final RevokeContext ctx) {
        PostgreSQLRevokeStatement result = new PostgreSQLRevokeStatement();
        Optional<Collection<SimpleTableSegment>> tableSegment = null == ctx.privilegeClause() ? Optional.empty() : getTableFromPrivilegeClause(ctx.privilegeClause());
        tableSegment.ifPresent(optional -> result.getTables().addAll(optional));
        return result;
    }
    
    @SuppressWarnings("unchecked")
    private Optional<Collection<SimpleTableSegment>> getTableFromPrivilegeClause(final PrivilegeClauseContext ctx) {
        if (null == ctx.onObjectClause() || null == ctx.onObjectClause().privilegeLevel()) {
            return Optional.empty();
        }
        if (null == ctx.onObjectClause().privilegeLevel().tableNames()) {
            return Optional.empty();
        }
        return Optional.of(((CollectionValue<SimpleTableSegment>) visit(ctx.onObjectClause().privilegeLevel().tableNames())).getValue());
    }
    
    @Override
    public ASTNode visitCreateUser(final CreateUserContext ctx) {
        return new PostgreSQLCreateUserStatement();
    }
    
    @Override
    public ASTNode visitDropUser(final DropUserContext ctx) {
        return new PostgreSQLDropUserStatement();
    }
    
    @Override
    public ASTNode visitAlterUser(final AlterUserContext ctx) {
        return new PostgreSQLAlterUserStatement();
    }
    
    @Override
    public ASTNode visitCreateRole(final CreateRoleContext ctx) {
        return new PostgreSQLCreateRoleStatement();
    }
    
    @Override
    public ASTNode visitAlterRole(final AlterRoleContext ctx) {
        return new PostgreSQLAlterRoleStatement();
    }
    
    @Override
    public ASTNode visitDropRole(final DropRoleContext ctx) {
        return new PostgreSQLDropRoleStatement();
    }
    
    @Override
    public ASTNode visitReassignOwned(final ReassignOwnedContext ctx) {
        return new PostgreSQLReassignOwnedStatement();
    }
    
    @Override
    public ASTNode visitCreateGroup(final CreateGroupContext ctx) {
        return new PostgreSQLCreateGroupStatement();
    }
}